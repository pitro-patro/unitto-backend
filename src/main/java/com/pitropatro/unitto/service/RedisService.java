package com.pitropatro.unitto.service;

import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: 삭제하기
@Service
public class RedisService {

    private RedisRepository redisRepository;

    @Autowired
    public RedisService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void insertKeyValue(String key, String value){

        if(redisRepository.getValueByKey(key)!=null)
            throw new DuplicateRedisKeyException();

        redisRepository.insertKeyValue(key, value);
    }

    public String getValueByKey(String key){
        String value = redisRepository.getValueByKey(key);

        if(value == null)
            throw new RedisKeyEmptyException();

        return value;
    }
}
