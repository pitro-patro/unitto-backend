package com.pitropatro.unitto.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmedUniqueNumber {
    private Long id;
    private Long userId;
    private Long lotteryRound;
    private String lotteryNumber;
    private LocalDateTime confirmDate;

    public ConfirmedUniqueNumber(Long userId, Long lotteryRound, String lotteryNumber, LocalDateTime confirmDate) {
        this.userId = userId;
        this.lotteryRound = lotteryRound;
        this.lotteryNumber = lotteryNumber;
        this.confirmDate = confirmDate;
    }
}
