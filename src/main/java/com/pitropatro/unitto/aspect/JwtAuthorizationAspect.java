package com.pitropatro.unitto.aspect;

import com.pitropatro.unitto.exception.token.EmptyTokenException;
import com.pitropatro.unitto.service.TokenService;
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

    private final TokenService tokenService;

    @Autowired
    public JwtAuthorizationAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@annotation(tokenRequired)")
    public void authenticateJwt(TokenRequired tokenRequired){
        // TODO: 출력 지우기
        System.out.println("authenticate JWT************************");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader("Authorization");
        if(!StringUtils.hasLength(token)){
            throw new EmptyTokenException();
        }

        String jwtToken = token.substring("Bearer ".length());
        System.out.println(jwtToken);

        Map<String, Object> jwtClaims = tokenService.verifyJwtAndReturnClaims(jwtToken);
        String userId = jwtClaims.get("id").toString();
        System.out.println(userId);

    }

    /*@Pointcut("execution(public * com.pitropatro.unitto.controller.lottery.*.*(..))")
    public void controllerPointcut(){}

    @Around("controllerPointcut()")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("AOP************************************");
        System.out.println(joinPoint.getSignature().getName());
        System.out.println(Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();
        return result;
    }*/
}
