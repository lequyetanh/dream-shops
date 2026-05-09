package com.dailycodework.dreamshops.payload.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BaseBadRequestException {
    String entityName;
    Object[] args;
    String errorKey;
    String originalMessage;

    private BaseBadRequestException(String entityName) {
        this.entityName = entityName;
    }

    private BaseBadRequestException(String entityName, String errorKey) {
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private BaseBadRequestException(String entityName, String errorKey, Object[] args) {
        this.entityName = entityName;
        this.args = args;
        this.errorKey = errorKey;
    }

    private BaseBadRequestException(String entityName, String errorKey, Object[] args, String originalMessage) {
        this.entityName = entityName;
        this.args = args;
        this.errorKey = errorKey;
        this.originalMessage = originalMessage;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
}
