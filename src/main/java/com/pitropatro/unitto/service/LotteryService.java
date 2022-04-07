package com.pitropatro.unitto.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryRoundNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.exception.lottery.NotExistingLotteryNumberException;
import com.pitropatro.unitto.exception.lottery.SaveConfirmedUniqueNumberFailedException;
import com.pitropatro.unitto.exception.lottery.UniqueNumberMaxTryException;
import com.pitropatro.unitto.exception.WrongApproachException;
import com.pitropatro.unitto.repository.ConfirmedUniqueNumberRepository;
import com.pitropatro.unitto.repository.RedisRepository;
import com.pitropatro.unitto.repository.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LotteryService {
    private final RedisRepository redisRepository;
    private final ConfirmedUniqueNumberRepository confirmedUniqueNumberRepository;

    @Value("${service.lottery.timeout}")
    private int TIMEOUT;
    @Value("${service.lottery.max_try}")
    private int MAXTRY;

    @Value("${service.lottery.round}")
    private int lotteryRound;
    @Value("${service.lottery.round_date}")
    private String lotteryRoundStartDate;

    @Autowired
    public LotteryService(RedisRepository redisRepository, ConfirmedUniqueNumberRepository confirmedUniqueNumberRepository) {
        this.redisRepository = redisRepository;
        this.confirmedUniqueNumberRepository = confirmedUniqueNumberRepository;
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

    public ConfirmedLotteryUniqueNumberDto confirmUniqueNumber(User userInfo, List<Integer> lotteryNumbers, Boolean confirm) {
        String lotteryNumberInString = lotteryNumbers.stream().map(String::valueOf).collect(Collectors.joining("-"));

        String lotteryNumberExistence = redisRepository.getValueByKey(lotteryNumberInString);
        if (lotteryNumberExistence == null) {   // 번호 추첨하지 않은 상태에서 접근
            throw new NotExistingLotteryNumberException();
        } else if (Boolean.parseBoolean(lotteryNumberExistence)) {  // 이미 사용중인 번호에 대한 접근
            throw new WrongApproachException();
        }

        if (confirm) {
            if(confirmedUniqueNumberRepository.saveConfirmedUniqueNumber(userInfo, getLotteryRound(), lotteryNumberInString)){
                redisRepository.insertKeyValue(lotteryNumberInString, "true");
            }
            else{
                throw new SaveConfirmedUniqueNumberFailedException();
            }

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

    public long getLotteryRound(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lastDate = LocalDateTime.parse(lotteryRoundStartDate, formatter);

        LocalDateTime currentDate = LocalDateTime.now();

        Duration duration = Duration.between(lastDate, currentDate);

        long dayDifference = duration.toDays();
        long currentLotteryRound = lotteryRound + dayDifference/7;

        return currentLotteryRound;
    }

    public LotteryRoundNumberDto getLotteryRoundNumber(String round) {

        List<Integer> lotteryRoundNumber = new ArrayList<>();
        int bonusNumber = 0;
        String roundDate = "";

        //https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=100
        String reqUrl = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber";

        try{
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(factory);

            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(reqUrl).append("&drwNo="+round);

            ResponseEntity<String> response = restTemplate.exchange(urlBuffer.toString(), HttpMethod.GET, null, String.class);

            //System.out.println("body: " + response.getBody());
            //body: {"totSellamnt":56561977000,"returnValue":"success","drwNoDate":"2004-10-30","firstWinamnt":3315315525,"drwtNo6":42,"drwtNo4":23,"firstPrzwnerCo":4,"drwtNo5":37,"bnusNo":6,"firstAccumamnt":0,"drwNo":100,"drwtNo2":7,"drwtNo3":11,"drwtNo1":1}

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.getBody());

            for(int i=1; i<7; i++){
                lotteryRoundNumber.add(element.getAsJsonObject().get("drwtNo"+i).getAsInt());
            }
            bonusNumber = element.getAsJsonObject().get("bnusNo").getAsInt();
            roundDate = element.getAsJsonObject().get("drwNoDate").getAsString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return new LotteryRoundNumberDto(lotteryRoundNumber, bonusNumber, roundDate);
    }
}
