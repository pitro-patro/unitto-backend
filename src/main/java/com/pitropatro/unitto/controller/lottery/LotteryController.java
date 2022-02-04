package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.controller.lottery.dto.ConfirmedLotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberConfirmDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
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



        return lotteryService.getUniqueNumber(
                lotteryUniqueNumberRequestDto.getIncludeNumbers(),
                lotteryUniqueNumberRequestDto.getExcludeNumbers());
    }

    @RequestMapping(value="/unique-numbers-confirm", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public ConfirmedLotteryUniqueNumberDto getUniqueNumberConfirm(@RequestBody LotteryUniqueNumberConfirmDto lotteryUniqueNumberConfirmDto){
        // TODO: Response에 확정된 로또 번호 추가해서 리턴해주기
        // TODO: + 취소할시 처리할 방법 필요

        return lotteryService.confirmUniqueNumber(
                lotteryUniqueNumberConfirmDto.getLotteryNumbers(),
                lotteryUniqueNumberConfirmDto.getConfirm());
    }
}
