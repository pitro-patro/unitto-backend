package com.pitropatro.unitto.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfiguration {

    @Value("${spring.mysql.driver_class_name}")
    private String driverClassName;
    @Value("${spring.mysql.url}")
    private String jdbcUrl;
    @Value("${spring.mysql.user_name}")
    private String userName;
    @Value("${spring.mysql.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource mysqlDataSource(){
        DataSource ds = new DataSource();
        ds.setDriverClassName(driverClassName);	//드라이버 클래스 지정(MySQL 드라이버 사용)
        ds.setUrl(jdbcUrl);	// JDBC URL 지정
        ds.setUsername(userName);	//DB 연결에 사용할 사용자 계정과 암호 지정
        ds.setPassword(password);
        ds.setInitialSize(2);
        ds.setMaxActive(10);

        ds.setTestWhileIdle(true);		// 유휴 커넥션 검사
        ds.setMinEvictableIdleTimeMillis(1000 * 60 * 3);	// 최소 유휴 시간 3분
        ds.setTimeBetweenEvictionRunsMillis(1000 * 10);		// 10초 주기

        return ds;
    }
}
