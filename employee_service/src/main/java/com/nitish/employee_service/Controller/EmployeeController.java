package com.nitish.employee_service.Controller;

import com.nitish.employee_service.component.JwtUtil;
import com.nitish.employee_service.model.Employee;
import com.nitish.employee_service.model.LoginRequest;
import com.nitish.employee_service.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nitish.employee_service.custom.CustomResponseEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    private List<Employee> employees = new ArrayList<Employee>();
    @Autowired
    RedisService redisService;

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

        Employee employee= null;
        for (Employee e : employees) {
            if(e.getEmail().equals(loginRequest.getUsername()) && e.getPassword().equals(loginRequest.getPassword())) {

                String jwt = jwtUtil.generateToken(loginRequest.getUsername());
                redisService.save(jwt, loginRequest.getUsername());
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "bearer " + jwt);
                customResponseEntity = new CustomResponseEntity(200, "Login successful", "Logged in successfully");
                return ResponseEntity.ok().headers(headers).body(customResponseEntity);
            }
        }
        System.out.println("Employee not found.");
        customResponseEntity = new CustomResponseEntity(404, "Employee not found", null);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/register")
    public ResponseEntity<CustomResponseEntity> registerEmployee(@RequestBody Employee employee) {
        CustomResponseEntity customResponseEntity;
        if(employee.getEmail() == null || employee.getPassword() == null || employee.getEmail().isEmpty() || employee.getPassword().isEmpty()) {
            customResponseEntity = new CustomResponseEntity(400, "Username or password is missing", null);
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        employee.setId((long) (employees.size() + 1));
        employee.setPassword(hash256(employee.getPassword()));
        employees.add(employee);
        customResponseEntity = new CustomResponseEntity(200, "Employee registered successfully", employee);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
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
