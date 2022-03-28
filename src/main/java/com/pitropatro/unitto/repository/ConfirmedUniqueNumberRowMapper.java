package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dto.ConfirmedUniqueNumber;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmedUniqueNumberRowMapper implements RowMapper<ConfirmedUniqueNumber> {
    @Override
    public ConfirmedUniqueNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
        ConfirmedUniqueNumber confirmedUniqueNumber = new ConfirmedUniqueNumber(
                rs.getLong("userId"),
                rs.getString("lotteryNumber"),
                rs.getTimestamp("confirmDate").toLocalDateTime()
        );
        confirmedUniqueNumber.setId(rs.getLong("id"));
        return confirmedUniqueNumber;
    }
}
