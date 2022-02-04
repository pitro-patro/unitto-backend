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

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    // TODO: insert에 대한 실패 성공 여부를 확인하는 방법이 있는지 확인하고 관련해서 Exception 혹은 어떻게는 처리할것
    public void insertKeyValue(String key, String value){
        valueOperations.set(key, value);
        //return void
    }

    public String getValueByKey(String key){
        return valueOperations.get(key);
    }

    public void insertKeyValueWithTimeout(String key, String value, int timeout){
        valueOperations.set(key, value, timeout, TimeUnit.SECONDS);
        //return void
    }

    public boolean deleteValueByKey(String key) {
        // TODO: 삭제할 대상이 없는 경우. 처리 필요한가?
        return redisTemplate.delete(key);
        // return null(값 없는 경우), true(삭제 성공시)
    }
}
