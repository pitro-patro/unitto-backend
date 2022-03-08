package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dao.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("refreshToken"),
                rs.getInt("refreshTokenExpire"),
                rs.getTimestamp("regdate").toLocalDateTime()
        );
        user.setId(rs.getLong("id"));
        return user;
    }
}