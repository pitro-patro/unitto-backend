package com.pitropatro.unitto.aspect;

import com.pitropatro.unitto.exception.token.EmptyTokenException;
import com.pitropatro.unitto.repository.dao.User;
import com.pitropatro.unitto.service.TokenService;
import com.pitropatro.unitto.service.UserService;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Aspect
@Component
public class JwtAuthorizationAspect {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public JwtAuthorizationAspect(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Around("@annotation(tokenRequired)")
    public Object authenticateJwt(ProceedingJoinPoint joinPoint, TokenRequired tokenRequired) throws Throwable {
        // TODO: 출력 지우기
        System.out.println("************************Authenticate JWT************************");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String bearerToken = request.getHeader("Authorization");
        User userInfo = userService.getUserInfoByToken(bearerToken);

        // 컨트롤러 메서드의 첫 파라미터를 사용자 정보로 설정한다
        Object [] args = joinPoint.getArgs();
        args[0] = userInfo;
        Object result = joinPoint.proceed(args);

        return result;
    }

}
