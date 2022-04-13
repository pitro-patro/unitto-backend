package com.pitropatro.unitto.aspect;

import com.pitropatro.unitto.repository.dto.User;
import com.pitropatro.unitto.service.TokenService;
import com.pitropatro.unitto.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
