package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dto.ConfirmedUniqueNumber;
import com.pitropatro.unitto.repository.dto.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ConfirmedUniqueNumberRepository {

    @Value("${service.lottery.round}")
    private int lotteryRound;
    @Value("${service.lottery.round_date}")
    private String lotteryRoundStartDate;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConfirmedUniqueNumberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean saveConfirmedUniqueNumber(User userInfo, String lotteryNumberInString) {
        int result = jdbcTemplate.update("INSERT INTO CONFIRMED_UNIQUE_NUMBER(userId, lotteryRound, lotteryNumber) VALUES (?, ?, ?)",
                userInfo.getId(),
                getLotteryRound(),
                lotteryNumberInString
                );

        return result == 1;
    }

    public List<ConfirmedUniqueNumber> getConfirmedUniqueNumberById(String userId) {
        List<ConfirmedUniqueNumber> results = jdbcTemplate.query("select * from CONFIRMED_UNIQUE_NUMBER where userId = ? ORDER BY confirmDate DESC",
                new ConfirmedUniqueNumberRowMapper(),
                userId);

        return results;
    }

    public long getLotteryRound(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lastDate = LocalDateTime.parse(lotteryRoundStartDate, formatter);

        LocalDateTime currentDate = LocalDateTime.now();

        Duration duration = Duration.between(lastDate, currentDate);

        long dayDifference = duration.toDays();
        long currentLotteryRound = lotteryRound + dayDifference/7;

        return currentLotteryRound;
    }
}
