package com.pitropatro.unitto.controller.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LotteryRoundNumberDto {
    private List<Integer> lotteryRoundNumbers;
    private int bonusNumber;
    private String roundDate;
}
