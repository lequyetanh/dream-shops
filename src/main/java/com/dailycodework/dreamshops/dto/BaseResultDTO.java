package com.dailycodework.dreamshops.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResultDTO {
    private Object message;
    private String reason;
    private boolean status = false;
    private Object data;
    private Integer count;

    public BaseResultDTO() {
    }

    public BaseResultDTO(Object message, boolean status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public BaseResultDTO(Object message, boolean status, Object data, Integer count) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.count = count;
    }


    public BaseResultDTO(Object message, String reason, boolean status) {
        this.message = message;
        this.reason = reason;
        this.status = status;
    }

    public BaseResultDTO(Object message, String reason, boolean status, Object data) {
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.data = data;
    }

    public BaseResultDTO(Object message, String reason, boolean status, Object data, Integer count) {
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.data = data;
        this.count = count;
    }

}
