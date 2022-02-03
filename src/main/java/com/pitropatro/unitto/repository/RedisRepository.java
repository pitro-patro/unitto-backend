package com.pitropatro.unitto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    // TODO: timeout은 Service layer에서 직접 넣어주게 바꾸자
    @Value("${redis.function.timeout}")
    private int timeout;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    // TODO: insert에 대한 실패 성공 여부를 확인하는 방법이 있는지 확인하고 관련해서 Exception 혹은 어떻게는 처리할것
    public void insertKeyValue(String key, String value){
        valueOperations.set(key, value);
    }

    public String getValueByKey(String key){
        return valueOperations.get(key);
    }

    // TODO: timeout은 사용하는 측에서 넣어주는게 맞는 것 같다.
    // TODO: insertKeyValueWithTimeout()로 바꾸기
    public void insertUniquNumberWithTimeout(String key){
        valueOperations.set(key, "false", timeout, TimeUnit.SECONDS);
    }

    public void deleteValueByKey(String key) {
        redisTemplate.delete(key);
    }
}
