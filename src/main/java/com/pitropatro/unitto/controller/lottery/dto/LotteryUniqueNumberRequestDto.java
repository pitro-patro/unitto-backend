package com.pitropatro.unitto.controller.lottery.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LotteryUniqueNumberRequestDto {
    private List<Integer> include_numbers;
    private List<Integer> exclude_numbers;
}
