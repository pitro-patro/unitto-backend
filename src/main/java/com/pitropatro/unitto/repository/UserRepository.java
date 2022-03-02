package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dao.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUserByEmail(String email){
        List<User> results = jdbcTemplate.query("select * from USER where email = ?",
                new UserRowMapper()
                , email);
        return results.isEmpty() ? null : results.get(0);
    }

    public User getUserById(String id) {
        List<User> results = jdbcTemplate.query("select * from USER where id = ?",
                new UserRowMapper()
                , id);
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean signUp(HashMap<String, Object> userInfo, HashMap<String, Object> tokenData) {
        int result = jdbcTemplate.update("INSERT INTO user(email, name, refreshToken, refreshTokenExpire) VALUES (?, ?, ?, ?)",
                userInfo.get("email"),
                userInfo.get("name"),
                tokenData.get("refreshToken"),
                tokenData.get("refreshTokenExpire")
                );

        return result == 1;
    }

}
