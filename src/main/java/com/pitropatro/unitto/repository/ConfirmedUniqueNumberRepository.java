package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dto.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmedUniqueNumberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired

    public ConfirmedUniqueNumberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean saveConfirmedUniqueNumber(User userInfo, String lotteryNumberInString) {
        int result = jdbcTemplate.update("INSERT INTO CONFIRMED_UNIQUE_NUMBER(userId, lotteryNumber) VALUES (?, ?)",
                userInfo.getId(),
                lotteryNumberInString
                );

        return result == 1;
    }
}
