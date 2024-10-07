package com.emlakjet.purchasing.controller.advice.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public class CommonResponseDTO<T> implements java.io.Serializable {

    private T data;
    private String message;

    public CommonResponseDTO(T data, int length, String message) {
        this.data = data;
        this.message = message;
    }

    public CommonResponseDTO(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public CommonResponseDTO(T data, Integer length) {
        this.data = data;
    }

    public CommonResponseDTO(T data) {
        this.data = data;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
