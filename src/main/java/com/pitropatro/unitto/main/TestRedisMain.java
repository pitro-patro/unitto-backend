package com.pitropatro.unitto.main;

import com.pitropatro.unitto.configuration.RedisConfiguration;
import com.pitropatro.unitto.service.DuplicateRedisKeyException;
import com.pitropatro.unitto.service.RedisKeyEmptyException;
import com.pitropatro.unitto.service.RedisService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class TestRedisMain {

    private static AnnotationConfigApplicationContext ctx = null;

    public static void main(String[] args){

        ArrayList<Integer> include_numbers = new ArrayList<>(Arrays.asList(1,2,3));
        ArrayList<Integer> exclude_numbers = new ArrayList<>(Arrays.asList(4,5,6));

        ArrayList<Integer> uniqueNumber = new ArrayList<>();

        Random random = new Random();

        int minNumber = 1;
        int maxNumber = 45;

        while(uniqueNumber.size() < 6){
            int randomNumber = (random.nextInt((maxNumber - minNumber) + 1) + minNumber);
            if(!uniqueNumber.contains(randomNumber)){
                uniqueNumber.add(randomNumber);
            }
        }
        Collections.sort(uniqueNumber);
        System.out.println(uniqueNumber);
        /*ctx = new AnnotationConfigApplicationContext(RedisConfiguration.class);
        RedisService redisService = ctx.getBean("redisService", RedisService.class);

        String key1 = "key1";
        String value1 = "value1";
        try{
            redisService.insertKeyValue(key1, value1);
            System.out.println("키-값 등록 완료");
        }catch(DuplicateRedisKeyException e){
            System.out.println("중복 키 발견!");
        }

        try{
            String val = redisService.getValueByKey(key1);
            System.out.println("값: "+val);
        }catch(RedisKeyEmptyException e){
            System.out.println("키에 대한 값 존재하지 않음!");
        }*/
    }
}
