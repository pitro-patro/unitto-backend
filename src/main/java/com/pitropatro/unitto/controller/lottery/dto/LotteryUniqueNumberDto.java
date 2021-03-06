package com.pitropatro.unitto.controller.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LotteryUniqueNumberDto {
    private List<Integer> uniqueLotteryNumbers;
    private int expireTime;
}
