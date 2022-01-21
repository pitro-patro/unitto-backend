package com.pitropatro.unitto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void insertKeyValue(String key, String value){
        
        //중복된 key가 있을 경우 처리 필요      //어디서 처리해야되는지 모르겠음
        valueOperations.set(key, value);
    }

    public String getValueByKey(String key){

        // 값이 nil인 경우 처리 필요
        return valueOperations.get(key);
    }
}
