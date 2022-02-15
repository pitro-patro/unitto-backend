package com.pitropatro.unitto.repository.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
    private String refreshToken;
    private LocalDateTime registerDateTime;

    public User(String email, String name, String refreshToken, LocalDateTime registerDateTime) {
        this.email = email;
        this.name = name;
        this.refreshToken = refreshToken;
        this.registerDateTime = registerDateTime;
    }
}
