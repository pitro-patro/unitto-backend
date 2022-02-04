package com.pitropatro.unitto.service;

import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LotteryService {
    private final RedisRepository redisRepository;

    @Value("${service.lottery.timeout}")
    private int TIMEOUT;
    @Value("${service.lottery.max_try}")
    private int MAXTRY;

    @Autowired
    public LotteryService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public LotteryUniqueNumberDto getUniqueNumber(List<Integer> includeNumbers, List<Integer> excludeNumbers) {
        //List<Integer> uniqueNumber = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));

        // TODO: MAX TRY를 두고 그만큼만 해보고 안되면 Exception 던지기
        for(int Tries = 0; Tries < MAXTRY; Tries++){
            List<Integer> uniqueNumber = getRandomLotteryNumberWithOptions(includeNumbers, excludeNumbers);
            String uniqueNumberInString = uniqueNumber.stream().map(String::valueOf).collect(Collectors.joining("-"));
            if(redisRepository.getValueByKey(uniqueNumberInString) == null){
                redisRepository.insertKeyValueWithTimeout(uniqueNumberInString, "false", TIMEOUT);
                return new LotteryUniqueNumberDto(uniqueNumber, TIMEOUT);
            }
        }

        return null;
    }

    public ConfirmedLotteryUniqueNumberDto confirmUniqueNumber(List<Integer> lotteryNumbers, Boolean confirm){
        String lotteryNumberInString = lotteryNumbers.stream().map(String::valueOf).collect(Collectors.joining("-"));

        String existingLotteryNumber = redisRepository.getValueByKey(lotteryNumberInString);
        System.out.println(existingLotteryNumber);
        if(existingLotteryNumber == null || existingLotteryNumber == "true"){
            return null;
        }
        // TODO : 이미 사용된 번호일 경우 confirm을 못하도록 방지해야 된다 ("true"를 감지 못하고 있다)
        if(existingLotteryNumber=="true"){
            return null;
        }

        if(confirm){
            redisRepository.insertKeyValue(lotteryNumberInString, "true");
            return new ConfirmedLotteryUniqueNumberDto(lotteryNumbers);
        }else{
            redisRepository.deleteValueByKey(lotteryNumberInString);
            // TODO: Confirm 취소시 처리할 방법 필요(현재는 null 리턴으로 처리함)
            return null;
        }
    }


    private ArrayList<Integer> getRandomLotteryNumberWithOptions(List<Integer> includeNumbers, List<Integer> excludeNumbers){
        // TODO: include, exclude할 번호에 대한 대략적인 rule을 정하기
        // include가 너무 많거나 하는 경우는 문제가 되겠지? 예를 들어 include가 10개인 경우 어쩔겨?
        // exclude가 39개인 경우 1가지 경우만 가능한데... 어쩔겨?
        // input즉 request body에 대한 validation을 추가해보자.
        // validation은 너가 정하기 나름. 알아서 잘 정하고 정의해보시길

        HashMap<Integer, Boolean> uniqueNumber = new HashMap<>();
        HashMap<Integer, Boolean> excludeNumberInHashMap = new HashMap<>();

        for(Integer number: includeNumbers){
            uniqueNumber.put(number, true);
        }
        for(Integer number: excludeNumbers){
            excludeNumberInHashMap.put(number, true);
        }

        Random random = new Random();

        int minNumber = 1;
        int maxNumber = 45;

        while(uniqueNumber.size() < 6){
            int randomNumber = (random.nextInt((maxNumber - minNumber) + 1) + minNumber);
            if((uniqueNumber.get(randomNumber)==null) && (excludeNumberInHashMap.get(randomNumber)==null)){
                uniqueNumber.put(randomNumber, true);
            }
        }
        ArrayList<Integer> uniqueNumberAsList = new ArrayList<>(uniqueNumber.keySet());
        Collections.sort(uniqueNumberAsList);
        return uniqueNumberAsList;
    }
}
