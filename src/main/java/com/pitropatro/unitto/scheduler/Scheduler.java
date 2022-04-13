package com.pitropatro.unitto.scheduler;

import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@EnableScheduling
@Component
public class Scheduler {
    private final RedisRepository redisRepository;

    @Autowired
    public Scheduler(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    // 0초 0분 20시  토요일
    @Scheduled(cron = "0 0 20 * * 6")
    public void resetWeekScheduler(){
        System.out.println("Saturday 8PM DB Reset Scheduler");
        redisRepository.flushAll();
    }
}
