package com.dailycodework.dreamshops.service.sequence;

import com.dailycodework.dreamshops.service.RedisManagementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SequenceService {
    private final String ENTITY_NAME = "sds.ep.BaseSequenceService";
    private final Logger log = LoggerFactory.getLogger(ENTITY_NAME);

    private final RedisManagementService redisManagementService;

    public String getSequenceCode(Long companyId, String code) {
        String keySequence = companyId + "-sequence";
        Object valueSequence = redisManagementService.getInHash(keySequence, code);
        if(valueSequence == null){
            redisManagementService.putToHash(keySequence, code, 1);
            return String.valueOf(1);
        }
        return String.valueOf(redisManagementService.increaseHash(keySequence, code, 1));
    }
}
