package com.emlakjet.purchasing.controller.advice;

import com.emlakjet.purchasing.controller.advice.Dto.CommonResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CustomResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (body instanceof CommonResponseDTO) {
            return body;
        } else if (mediaType.equals(MediaType.TEXT_PLAIN) || mediaType.equals(MediaType.APPLICATION_JSON)) {
            return body;
        } else {
            return new CommonResponseDTO<>(body);
        }
    }
}