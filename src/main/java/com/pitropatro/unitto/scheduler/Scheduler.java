package com.pitropatro.unitto.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@EnableScheduling
@Component
public class Scheduler {

    // cron = "초 분 시 일 월 요일"
    @Scheduled(cron = "10 * * * * *")
    public void schedulerTest(){
        System.out.println("*****************SCHEDULER Current Time : "+ new Date());
    }
}
