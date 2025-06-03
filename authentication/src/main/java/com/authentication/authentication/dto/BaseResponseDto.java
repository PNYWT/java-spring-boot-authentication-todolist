package com.authentication.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDto<T> {
    private String ret;
    private String message;
    private T data;

    public BaseResponseDto setBaseResponse(String ret, String message) {
        this.ret = ret;
        this.message = message;
        return this;
    }

    public BaseResponseDto<T> setBaseReposeData(String ret, String message, T data) {
        this.ret = ret;
        this.message = message;
        this.data = data;
        return this;
    }
}
