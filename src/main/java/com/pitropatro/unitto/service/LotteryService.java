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

    @Autowired
    public LotteryService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public LotteryUniqueNumberDto getUniqueNumber(List<Integer> includeNumbers, List<Integer> exclude_numbers) {
        //List<Integer> uniqueNumber = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));

        // TODO: MAX TRY를 두고 그만큼만 해보고 안되면 Exception 던지기
        do{
            List<Integer> uniqueNumber = getRandomLotteryNumberWithOptions(includeNumbers, exclude_numbers);
            String uniqueNumberInString = uniqueNumber.stream().map(String::valueOf).collect(Collectors.joining("-"));
            if(redisRepository.getValueByKey(uniqueNumberInString) == null){
                redisRepository.insertKeyValueWithTimeout(uniqueNumberInString, false, 300);
                redisRepository.insertUniquNumberWithTimeout(uniqueNumberInString);
                return new LotteryUniqueNumberDto(uniqueNumber);
            }
        }while(true);

    }

    public void confirmUniqueNumber(List<Integer> lottery_numbers, Boolean confirm){
        String lotteryNumberInString = lottery_numbers.stream().map(String::valueOf).collect(Collectors.joining("-"));
        // Confirm하기전에 무언가 확인해줘야하지 않을까?
        // 왜 처음에 false를 넣고 timeout을 넣었을까?
        // redis에 해당 번호가 생성이 되었었는지 먼저 확인해야겠지?

        if(confirm){
            redisRepository.insertKeyValue(lotteryNumberInString, "true");
        }else{
            redisRepository.deleteValueByKey(lotteryNumberInString);
        }
    }


    private ArrayList<Integer> getRandomLotteryNumberWithOptions(List<Integer> includeNumbers, List<Integer> exclude_numbers){
        // TODO: include, exclude할 번호에 대한 대략적인 rule을 정하기
        // include가 너무 많거나 하는 경우는 문제가 되겠지? 예를 들어 include가 10개인 경우 어쩔겨?
        // exclude가 39개인 경우 1가지 경우만 가능한데... 어쩔겨?
        // input즉 request body에 대한 validation을 추가해보자.
        // validation은 너가 정하기 나름. 알아서 잘 정하고 정의해보시길

        HashMap<Integer, Boolean> uniqueNumber = new HashMap<>();

        for(Integer number: includeNumbers){
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
