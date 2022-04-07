package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dto.ConfirmedUniqueNumber;
import com.pitropatro.unitto.repository.dto.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfirmedUniqueNumberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConfirmedUniqueNumberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean saveConfirmedUniqueNumber(User userInfo, Long lotteryRound, String lotteryNumberInString) {
        int result = jdbcTemplate.update("INSERT INTO CONFIRMED_UNIQUE_NUMBER(userId, lotteryRound, lotteryNumber) VALUES (?, ?, ?)",
                userInfo.getId(),
                lotteryRound,
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

}
