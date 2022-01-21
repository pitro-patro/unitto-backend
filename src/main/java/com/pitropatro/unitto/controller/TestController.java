package com.pitropatro.unitto.controller;

import com.pitropatro.unitto.service.DuplicateRedisKeyException;
import com.pitropatro.unitto.service.RedisKeyEmptyException;
import com.pitropatro.unitto.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class TestController {

    private RedisService redisService;

    @Autowired
    public TestController(RedisService redisService){
        this.redisService = redisService;
    }

    @RequestMapping("/insertKeyValue")
    public String insertKeyValue(){

        try{
            redisService.insertKeyValue("test1","test_val1");
        } catch(DuplicateRedisKeyException e){
            System.out.println("Duplicate KEY Exists");
        }


        return "TestDone";
    }

    @RequestMapping(value = "/getValueByKey", method = RequestMethod.GET)
    public String getValueByKey(){

        try{
            String a = redisService.getValueByKey("test1");
            System.out.println(a);
            return a;
        } catch(RedisKeyEmptyException e){
            System.out.println("KEY Does not Exists");
            return null;
        }
    }
}
