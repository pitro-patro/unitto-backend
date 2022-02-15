package com.pitropatro.unitto.repository;

import com.pitropatro.unitto.repository.dao.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MysqlRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MysqlRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUserByEmail(String email){
        List<User> results = jdbcTemplate.query("select * from USER where email = ?",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User(
                                rs.getString("email"),
                                rs.getString("name"),
                                rs.getString("refreshToken"),
                                rs.getTimestamp("regdate").toLocalDateTime()
                        );
                        user.setId(rs.getLong("id"));
                        return user;
                    }
                }, email);
        return results.isEmpty() ? null : results.get(0);
    }
}
