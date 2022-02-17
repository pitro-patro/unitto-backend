package com.pitropatro.unitto.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${auth.token.secret-key}")
    private String secretKey;
    @Value("${auth.token.expire-time}")
    private Long tokenExpireTime;

    public String createJwtToken(Long id) {
        // Token Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // Token Payload
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("id", id);

        // Token expire date
        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + tokenExpireTime);

        // Build Token
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user") // Token Usage
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact(); // Create Token

    }
}
