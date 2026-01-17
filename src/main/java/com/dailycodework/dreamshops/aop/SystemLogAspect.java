package com.dailycodework.dreamshops.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class SystemLogAspect {
    private final Logger log = LoggerFactory.getLogger(SystemLogAspect.class);
    private final HttpServletRequest request;
    private String type;
    private Boolean isMultipleData;
    private String actionType;

    //  Định nghĩa điểm cắt (pointcut) trong AOP
    @Pointcut("@annotation(com.dailycodework.dreamshops.aop.WriteSystemLog)")
    public void writeSystemLog() {}

    @Around("writeSystemLog()")
    public Object aroundWriteSystemLog(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        WriteSystemLog myAnnotation = method.getAnnotation(WriteSystemLog.class);
        type = myAnnotation.type();
        isMultipleData = myAnnotation.isMultipleData();
        actionType = myAnnotation.actionType();
        return pjp.proceed();
    }

    @AfterReturning(pointcut = "writeSystemLog()", returning = "result")
    public void afterReturningWriteSystemLog(ProceedingJoinPoint pjp, Object result) {
        // Implementation for logging after method execution
        log.info("System log - Type: {}, IsMultipleData: {}, ActionType: {}", type, isMultipleData, actionType);
        // Additional logging logic can be added here
    }
}
