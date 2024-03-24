package com.nitish.employee_service.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {

    private RedisTemplate<String, Object> template;
    private HashOperations hashOperations;

    @Autowired
    public RedisService(RedisTemplate<String, Object> template){
        this.template = template;
        this.hashOperations = template.opsForHash();
    }

    public void setKeyValue(String key, String username, String privilege) {
        hashOperations.put(key, "username", username);
        hashOperations.put(key, "privilege", privilege);
    }

    public String getUsername(String key) {
        return (String) hashOperations.get(key, "username");
    }
    public String getPrivilege(String key) {
        return (String) hashOperations.get(key, "privilege");
    }
    public void deleteKey(String key) {
        template.delete(key);
    }
    public Map<Object, Object> getKeyValue(String key) {
        return hashOperations.entries(key);
    }
}