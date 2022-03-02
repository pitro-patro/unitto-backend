package com.pitropatro.unitto.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class JwtAuthorizationAspect {

    @Before("@annotation(tokenRequired)")
    public void authenticateJwt(TokenRequired tokenRequired){
        System.out.println("authenticate JWT************************");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader("Authorization");
        System.out.println(token);
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
