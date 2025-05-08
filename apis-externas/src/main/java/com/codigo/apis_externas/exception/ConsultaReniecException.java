package com.codigo.apis_externas.exception;

public class ConsultaReniecException extends RuntimeException {

    public ConsultaReniecException(String message) {
        super(message);
    }

    public ConsultaReniecException(String message, Throwable cause) {
        super(message, cause);
    }
}
