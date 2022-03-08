package com.pitropatro.unitto.controller.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String email;
    private String name;
    private String jwtToken;
}
