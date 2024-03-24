package com.nitish.employee_service.Controller;

import com.nitish.employee_service.component.JwtUtil;
import com.nitish.employee_service.model.Employee;
import com.nitish.employee_service.model.LoginRequest;
import com.nitish.employee_service.services.EmployeeServices;
import com.nitish.employee_service.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nitish.employee_service.custom.CustomResponseEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    RedisService redisService;

    @Autowired
    EmployeeServices employeeServices;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<CustomResponseEntity> login(@RequestBody LoginRequest loginRequest) {
        CustomResponseEntity customResponseEntity ;
        if(loginRequest.getUsername() == null || loginRequest.getPassword() == null || loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            customResponseEntity = new CustomResponseEntity(400, "Username or password is missing", null);
            return ResponseEntity.badRequest().body(customResponseEntity);
        }

        loginRequest.setPassword(hash256(loginRequest.getPassword()));
        System.out.println("Login request received: " + loginRequest.getUsername() + " " + loginRequest.getPassword());

        Employee e = employeeServices.getEmployeePassword(loginRequest.getUsername());
        if(e!=null && e.getPassword().equals(loginRequest.getPassword()) && e.getEmail().equals(loginRequest.getUsername())) {

                String jwt = jwtUtil.generateToken(loginRequest.getUsername());

                redisService.setKeyValue(jwt, loginRequest.getUsername(), e.getPrivilege().toString());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "bearer " + jwt);
                customResponseEntity = new CustomResponseEntity(200, "Login successful", "Logged in successfully");
                return ResponseEntity.ok().headers(headers).body(customResponseEntity);
        }

        System.out.println("Employee not found.");
        customResponseEntity = new CustomResponseEntity(404, "Employee not found", null);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/register")
    public ResponseEntity<CustomResponseEntity> registerEmployee(@RequestBody Employee employee, @RequestHeader("Authorization") String token){
        if(!redisService.getPrivilege(token).equals("ADMIN")){
            CustomResponseEntity customResponseEntity = new CustomResponseEntity(403, "Unauthorized", null);
            return new ResponseEntity<>(customResponseEntity, HttpStatus.FORBIDDEN);
        }

        CustomResponseEntity customResponseEntity;
        if(employee.getEmail() == null || employee.getPassword() == null || employee.getEmail().isEmpty() || employee.getPassword().isEmpty() || employee.getPrivilege() == null){
            customResponseEntity = new CustomResponseEntity(400, "Username or password is missing", null);
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }

        employee.setPassword(hash256(employee.getPassword()));
        if(employeeServices.saveEmployee(employee)==null){
            customResponseEntity = new CustomResponseEntity(400, "Employee registration failed", null);
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }

        customResponseEntity = new CustomResponseEntity(200, "Employee registered successfully", employee);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<CustomResponseEntity> getEmployee(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        // only return employee if token and id assigned to the employee are same
        if(!redisService.getUsername(token).equals(employeeServices.getEmployeeById(id).getEmail())){
            return new ResponseEntity<>(new CustomResponseEntity(403, "Unauthorized", null), HttpStatus.FORBIDDEN);
        }else{
            return new ResponseEntity<>(new CustomResponseEntity(200, "Employee found", employeeServices.getEmployeeById(id)), HttpStatus.OK);
        }
    }
    @GetMapping("/employee/all")
    public ResponseEntity<CustomResponseEntity> getEmployees(@RequestHeader("Authorization") String token) {
        //only return employees list if token is assigned to admin or a higher authority

        if(!redisService.getPrivilege(token).equals("ADMIN") || !redisService.getPrivilege(token).equals("MANAGER")){
            CustomResponseEntity customResponseEntity = new CustomResponseEntity(403, "Unauthorized", null);
            return new ResponseEntity<>(customResponseEntity, HttpStatus.FORBIDDEN);
        }else{
            List<Employee> employees = new ArrayList<>();
            employeeServices.getAllEmployees().forEach(employees::add);
            return new ResponseEntity<>(new CustomResponseEntity(200, "Employees found", employees), HttpStatus.OK);
        }

    }
    @PutMapping("/employee/{id}")
    public ResponseEntity<CustomResponseEntity> updateEmployee(@PathVariable Long id, @RequestBody Employee employee,@RequestHeader("Authorization") String token) {
        //only  update employee if token is of higher authority or same as the employee
        if(!redisService.getUsername(token).equals(employeeServices.getEmployeeById(id).getEmail())){
            return new ResponseEntity<>(new CustomResponseEntity(403, "Unauthorized", null), HttpStatus.FORBIDDEN);
        }
        employeeServices.updateEmployee(employee);
        return new ResponseEntity<>(new CustomResponseEntity(200, "Employee updated successfully", employee), HttpStatus.OK);
    }
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<CustomResponseEntity> deleteEmployee(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        //only delete employee if token is of higher authority or same as the employee
        if( !redisService.getPrivilege(token).equals("ADMIN") || !redisService.getPrivilege(token).equals("MANAGER")){
            return new ResponseEntity<>(new CustomResponseEntity(403, "Unauthorized", null), HttpStatus.FORBIDDEN);
        }
        employeeServices.deleteEmployee(id);
        return new ResponseEntity<>(new CustomResponseEntity(200, "Employee deleted successfully", null), HttpStatus.OK);

    }
    @GetMapping("/logout")
    public ResponseEntity<CustomResponseEntity>  logout(@RequestHeader("Authorization") String token) {
        redisService.deleteKey(token);
        return new ResponseEntity<>(new CustomResponseEntity(200, "Logged out successfully", null), HttpStatus.OK);
    }
    public String hash256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
/*
{
 key: {
    username: value,
    privilege: value
 }
}


*/