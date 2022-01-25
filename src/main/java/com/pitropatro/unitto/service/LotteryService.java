package com.pitropatro.unitto.service;

import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LotteryService {
    private final RedisRepository redisRepository;
    private final RedisService redisService;

    @Autowired
    public LotteryService(RedisRepository redisRepository, RedisService redisService) {
        this.redisRepository = redisRepository;
        this.redisService = redisService;
    }

    public LotteryUniqueNumberDto getUniqueNumber(List<Integer> include_numbers, List<Integer> exclude_numbers) {
        //List<Integer> uniqueNumber = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));

        do{
            List<Integer> uniqueNumber = getRandomLotteryNumberWithOptions(include_numbers, exclude_numbers);
            String uniqueNumberInString = uniqueNumber.stream().map(String::valueOf).collect(Collectors.joining("-"));
            if(redisRepository.getValueByKey(uniqueNumberInString) == null){
                System.out.println("It is Unique Number!");
                redisRepository.insertUniquNumberWithTimeout(uniqueNumberInString);
                return new LotteryUniqueNumberDto(uniqueNumber);
            }
            System.out.println(uniqueNumberInString+" already exists!! Find new Number");
        }while(true);

    }

    public void confirmUniqueNumber(List<Integer> lottery_numbers, Boolean confirm){
        String lotteryNumberInString = lottery_numbers.stream().map(String::valueOf).collect(Collectors.joining("-"));
        //System.out.println(lotteryNumberInString);
        if(confirm){
            redisRepository.insertKeyValue(lotteryNumberInString, "true");
        }else{
            redisRepository.deleteValueByKey(lotteryNumberInString);
        }
    }

    public ArrayList<Integer> getRandomLotteryNumberWithOptions(List<Integer> include_numbers, List<Integer> exclude_numbers){
        HashMap<Integer, Boolean> uniqueNumber = new HashMap<>();

        for(Integer number: include_numbers){
            uniqueNumber.put(number, true);
        }
        Random random = new Random();

        int minNumber = 1;
        int maxNumber = 45;

        while(uniqueNumber.size() < 6){
            int randomNumber = (random.nextInt((maxNumber - minNumber) + 1) + minNumber);
            if((uniqueNumber.get(randomNumber)==null) && !exclude_numbers.contains(randomNumber)){
                uniqueNumber.put(randomNumber, true);
            }
        }
        ArrayList<Integer> uniqueNumberAsList = new ArrayList<>(uniqueNumber.keySet());
        Collections.sort(uniqueNumberAsList);
        return uniqueNumberAsList;
    }
}
