package com.emlakjet.purchasing.controller.advice;

import com.emlakjet.purchasing.controller.advice.Dto.CommonResponseDTO;
import com.emlakjet.purchasing.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponseDTO<List<StackTraceElement>> processAllError(Exception ex) {

        CommonResponseDTO<List<StackTraceElement>> response = new CommonResponseDTO<>(null, ex.getMessage());
        return response;

    }


}
