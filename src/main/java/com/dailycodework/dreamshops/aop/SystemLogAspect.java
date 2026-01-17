package com.dailycodework.dreamshops.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SystemLogAspect {
    private final Logger log = LoggerFactory.getLogger(SystemLogAspect.class);
    private final HttpServletRequest request;
}
