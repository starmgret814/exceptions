package com.codigo.retrofit.controller.advice;

import com.codigo.retrofit.aggregates.exception.ConsultaReniecException;
import com.codigo.retrofit.aggregates.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handleConsultaReniecException(
            Throwable ex){
        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(ex.getClass().getSimpleName(), ex.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(ex instanceof ConsultaReniecException){
            status = HttpStatus.BAD_GATEWAY;
        }

        return ResponseEntity.status(status).body(apiErrorResponse);
    }
}
