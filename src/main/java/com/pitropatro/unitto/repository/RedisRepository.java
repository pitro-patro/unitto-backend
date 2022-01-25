package com.pitropatro.unitto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @Value("${redis.function.timeout}")
    private int timeout;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void insertKeyValue(String key, String value){
        valueOperations.set(key, value);
    }

    public String getValueByKey(String key){
        return valueOperations.get(key);
    }

    public void insertUniquNumberWithTimeout(String key){
        valueOperations.set(key, "false", timeout, TimeUnit.SECONDS);
    }

    public void deleteValueByKey(String key) {
        redisTemplate.delete(key);
    }
}
