package com.dailycodework.dreamshops.service.logger;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SyslogService {
    private final Logger log = LoggerFactory.getLogger(SyslogService.class);
}
