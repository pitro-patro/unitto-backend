package com.pitropatro.unitto.service;

import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.exception.lottery.NotExistingLotteryNumberException;
import com.pitropatro.unitto.exception.lottery.UniqueNumberMaxTryException;
import com.pitropatro.unitto.exception.WrongApproachException;
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

        for (int Tries = 0; Tries < MAXTRY; Tries++) {
            List<Integer> uniqueNumber = getRandomLotteryNumberWithOptions(includeNumbers, excludeNumbers);
            String uniqueNumberInString = uniqueNumber.stream().map(String::valueOf).collect(Collectors.joining("-"));
            if (redisRepository.getValueByKey(uniqueNumberInString) == null) {
                redisRepository.insertKeyValueWithTimeout(uniqueNumberInString, "false", TIMEOUT);
                return new LotteryUniqueNumberDto(uniqueNumber, TIMEOUT);
            }
        }

        throw new UniqueNumberMaxTryException();
    }

    public ConfirmedLotteryUniqueNumberDto confirmUniqueNumber(List<Integer> lotteryNumbers, Boolean confirm) {
        String lotteryNumberInString = lotteryNumbers.stream().map(String::valueOf).collect(Collectors.joining("-"));

        String lotteryNumberExistence = redisRepository.getValueByKey(lotteryNumberInString);
        if (lotteryNumberExistence == null) {   // 번호 추첨하지 않은 상태에서 접근
            throw new NotExistingLotteryNumberException();
        } else if (Boolean.parseBoolean(lotteryNumberExistence)) {  // 이미 사용중인 번호에 대한 접근
            throw new WrongApproachException();
        }

        if (confirm) {
            redisRepository.insertKeyValue(lotteryNumberInString, "true");
            return new ConfirmedLotteryUniqueNumberDto(lotteryNumbers);
        } else {
            redisRepository.deleteValueByKey(lotteryNumberInString);
            return null;
        }
    }


    private ArrayList<Integer> getRandomLotteryNumberWithOptions(List<Integer> includeNumbers, List<Integer> excludeNumbers) {

        HashMap<Integer, Boolean> uniqueNumber = new HashMap<>();
        HashMap<Integer, Boolean> excludeNumberInHashMap = new HashMap<>();

        for (Integer number : includeNumbers) {
            uniqueNumber.put(number, true);
        }
        for (Integer number : excludeNumbers) {
            excludeNumberInHashMap.put(number, true);
        }

        Random random = new Random();

        int minNumber = 1;
        int maxNumber = 45;

        // Controller에서 includeNumbers, excludeNumbers 크기에 대한 Validation 처리를 하므로 크기관련 예외처리 필요없음
        while (uniqueNumber.size() < 6) {
            int randomNumber = (random.nextInt((maxNumber - minNumber) + 1) + minNumber);
            if ((uniqueNumber.get(randomNumber) == null) && (excludeNumberInHashMap.get(randomNumber) == null)) {
                uniqueNumber.put(randomNumber, true);
            }
        }
        ArrayList<Integer> uniqueNumberAsList = new ArrayList<>(uniqueNumber.keySet());
        Collections.sort(uniqueNumberAsList);
        return uniqueNumberAsList;
    }
}
