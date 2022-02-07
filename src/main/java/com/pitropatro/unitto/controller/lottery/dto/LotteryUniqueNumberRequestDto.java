package com.pitropatro.unitto.controller.lottery.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LotteryUniqueNumberRequestDto {
    @Size(max=5)
    private List<Integer> includeNumbers;
    @Size(max=39)
    private List<Integer> excludeNumbers;
}
