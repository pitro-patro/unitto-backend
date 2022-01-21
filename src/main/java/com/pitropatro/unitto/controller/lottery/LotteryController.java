package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
import com.pitropatro.unitto.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
