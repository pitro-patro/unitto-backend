package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.aspect.TokenRequired;
import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberConfirmDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
import com.pitropatro.unitto.exception.lottery.LotteryNumberOptionSizeException;
import com.pitropatro.unitto.repository.dao.User;
import com.pitropatro.unitto.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService){
        this.lotteryService = lotteryService;
    }

    // TODO: @JwtAuthorizationAspect에 따라 모든 메소드의 첫 파라미터는 User가 필요하다
    @TokenRequired
    @RequestMapping(value="/unique-numbers", method= RequestMethod.POST)
    public LotteryUniqueNumberDto getUniqueNumber(User userInfo, @Valid @RequestBody LotteryUniqueNumberRequestDto lotteryUniqueNumberRequestDto, Errors errors){

        // LotteryUniqueNumberRequestDto Validation 처리
        if(errors.hasErrors()){
            throw new LotteryNumberOptionSizeException();
        }

        System.out.println("Current USER: "+userInfo.getEmail());

        return lotteryService.getUniqueNumber(
                lotteryUniqueNumberRequestDto.getIncludeNumbers(),
                lotteryUniqueNumberRequestDto.getExcludeNumbers());
    }

    @TokenRequired
    @RequestMapping(value="/unique-numbers-confirm", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public ConfirmedLotteryUniqueNumberDto getUniqueNumberConfirm(User userInfo, @RequestBody LotteryUniqueNumberConfirmDto lotteryUniqueNumberConfirmDto){

        System.out.println("서비스 이용 사용자: "+userInfo.getEmail());

        return lotteryService.confirmUniqueNumber(
                lotteryUniqueNumberConfirmDto.getLotteryNumbers(),
                lotteryUniqueNumberConfirmDto.getConfirm());
    }
}
