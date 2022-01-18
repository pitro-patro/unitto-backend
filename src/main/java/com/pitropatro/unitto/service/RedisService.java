package com.pitropatro.unitto.service;

import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedisService {

    @Autowired
    private RedisRepository redisRepository;

    @Transactional
    public void insertKeyValue(String key, String value){

        if(redisRepository.getValueByKey(key) != null)
            throw new DuplicateRedisKeyException();

        redisRepository.insertKeyValue(key, value);
    }

    @Transactional
    public String getValueByKey(String key){
        String value = redisRepository.getValueByKey(key);

        if(value == null)
            throw new RedisKeyEmptyException();

        return value;
    }
}
