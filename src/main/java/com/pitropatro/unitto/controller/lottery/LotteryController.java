package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberConfirmDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
import com.pitropatro.unitto.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService){
        this.lotteryService = lotteryService;
    }

    @RequestMapping(value="/unique-numbers", method= RequestMethod.POST)
    @ResponseBody
    public LotteryUniqueNumberDto getUniqueNumber(@RequestBody LotteryUniqueNumberRequestDto lotteryUniqueNumberRequestDto){

        return lotteryService.getUniqueNumber(
                lotteryUniqueNumberRequestDto.getInclude_numbers(),
                lotteryUniqueNumberRequestDto.getExclude_numbers());
    }

    @RequestMapping(value="/unique-numbers-confirm", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public void getUniqueNumberConfirm(@RequestBody LotteryUniqueNumberConfirmDto lotteryUniqueNumberConfirmDto){
        lotteryService.confirmUniqueNumber(lotteryUniqueNumberConfirmDto.getLottery_numbers(), lotteryUniqueNumberConfirmDto.getConfirm());
    }
}
