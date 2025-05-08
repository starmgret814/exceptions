package com.codigo.apis_externas.aggregates.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ResponseBase<T> {
    private Integer code;
    private String message;
    private Optional<T> entity;
}
