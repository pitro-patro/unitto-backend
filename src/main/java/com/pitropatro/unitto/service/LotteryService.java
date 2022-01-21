package com.pitropatro.unitto.service;

import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class LotteryService {
    private final RedisRepository redisRepository;

    @Autowired
    public LotteryService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public LotteryUniqueNumberDto getUniqueNumber(List<Integer> include_numbers, List<Integer> exclude_numbers) {
        List<Integer> uniqueNumber = getRandomLotteryNumberWithOptions(include_numbers, exclude_numbers);

        return new LotteryUniqueNumberDto(uniqueNumber);
    }

    public ArrayList<Integer> getRandomLotteryNumberWithOptions(List<Integer> include_numbers, List<Integer> exclude_numbers){
        ArrayList<Integer> uniqueNumber = (ArrayList<Integer>)include_numbers;

        Random random = new Random();

        int minNumber = 1;
        int maxNumber = 45;

        while(uniqueNumber.size() < 6){
            int randomNumber = (random.nextInt((maxNumber - minNumber) + 1) + minNumber);
            if(!exclude_numbers.contains(randomNumber) && !uniqueNumber.contains(randomNumber)){
                uniqueNumber.add(randomNumber);
            }
        }
        Collections.sort(uniqueNumber);
        return uniqueNumber;
    }
}
