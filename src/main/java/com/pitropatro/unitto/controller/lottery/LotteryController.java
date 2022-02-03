package com.pitropatro.unitto.controller.lottery;

import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberConfirmDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberDto;
import com.pitropatro.unitto.controller.lottery.dto.LotteryUniqueNumberRequestDto;
import com.pitropatro.unitto.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// TODO: RestController로 바꾸기
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
                lotteryUniqueNumberRequestDto.getIncludeNumbers(),
                lotteryUniqueNumberRequestDto.getExclude_numbers());
    }

    @RequestMapping(value="/unique-numbers-confirm", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public void getUniqueNumberConfirm(@RequestBody LotteryUniqueNumberConfirmDto lotteryUniqueNumberConfirmDto){
        // TODO: Response에 확정된 로또 번호 추가해서 리턴해주기
        lotteryService.confirmUniqueNumber(lotteryUniqueNumberConfirmDto.getLottery_numbers(), lotteryUniqueNumberConfirmDto.getConfirm());
    }
}
