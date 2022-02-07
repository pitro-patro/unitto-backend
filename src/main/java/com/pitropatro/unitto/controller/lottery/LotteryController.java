package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberConfirmDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
import com.pitropatro.unitto.exception.LotteryNumberOptionSizeException;
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

    @RequestMapping(value="/unique-numbers", method= RequestMethod.POST)
    public LotteryUniqueNumberDto getUniqueNumber(@Valid @RequestBody LotteryUniqueNumberRequestDto lotteryUniqueNumberRequestDto, Errors errors){

        // LotteryUniqueNumberRequestDto Validation 처리
        if(errors.hasErrors()){
            throw new LotteryNumberOptionSizeException();
        }

        return lotteryService.getUniqueNumber(
                lotteryUniqueNumberRequestDto.getIncludeNumbers(),
                lotteryUniqueNumberRequestDto.getExcludeNumbers());
    }

    @RequestMapping(value="/unique-numbers-confirm", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public ConfirmedLotteryUniqueNumberDto getUniqueNumberConfirm(@RequestBody LotteryUniqueNumberConfirmDto lotteryUniqueNumberConfirmDto){

        return lotteryService.confirmUniqueNumber(
                lotteryUniqueNumberConfirmDto.getLotteryNumbers(),
                lotteryUniqueNumberConfirmDto.getConfirm());
    }
}
