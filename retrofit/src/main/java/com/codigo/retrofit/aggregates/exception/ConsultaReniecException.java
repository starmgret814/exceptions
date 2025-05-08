package com.codigo.retrofit.aggregates.exception;

public class ConsultaReniecException extends RuntimeException {

    public ConsultaReniecException(String message) {
        super(message);
    }

    public ConsultaReniecException(String message, Throwable cause) {
        super(message, cause);
    }

}