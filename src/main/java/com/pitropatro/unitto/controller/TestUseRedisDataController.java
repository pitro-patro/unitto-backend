package com.pitropatro.unitto.controller;

import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestUseRedisDataController {

    @Autowired
    private RedisRepository redisRepository;

    @RequestMapping(value="", method = RequestMethod.POST)
    public String setRedisKeyValue(
            @RequestParam("key") String key,
            @RequestParam("value") String value
            ){
        //System.out.println("/test POST getRedisValueByKey" + "//"+key+value);
        redisRepository.insertKeyValue(key, value);
        return "키: " + key + "값: "+value;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public String getRedisValueByKey(
            @RequestParam("key") String key
    ){
        System.out.println("/test GET getRedisValueByKey");
        String value = redisRepository.getValueByKey(key);
        return value;
    }

}
