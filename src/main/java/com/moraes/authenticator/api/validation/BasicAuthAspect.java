package com.moraes.authenticator.api.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.moraes.authenticator.api.service.BasicTokenService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class BasicAuthAspect {

    private final BasicTokenService basicTokenService;

    /**
     * This code snippet is an Aspect-Oriented Programming (AOP) aspect in Java that
     * uses Spring AOP annotations.
     * 
     * Here's what it does:
     * 
     * It intercepts (or "advises") any method that is annotated
     * with @IRequireBasicAuth.
     * When such a method is called, it first calls the validateBasicToken() method
     * on the basicTokenService object.
     * If validateBasicToken() does not throw an exception, it then proceeds to call
     * the original method that was intercepted, using joinPoint.proceed().
     * In essence, this aspect ensures that the validateBasicToken() method is
     * called before any method that requires basic authentication.
     * 
     * @param joinPoint the target annotated with IRequireBasicAuth annotation
     * @return the result of the target method
     * @throws Throwable if any error occurs
     */
    @Around("@annotation(com.moraes.authenticator.api.validation.interfaces.IRequireBasicAuth)")
    public Object validateBasicAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        basicTokenService.validateBasicToken();
        return joinPoint.proceed();
    }
}
