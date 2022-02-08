package com.pitropatro.unitto.controller.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LotteryUniqueNumberConfirmDto {
    private List<Integer> lotteryNumbers;
    private Boolean confirm;
}
