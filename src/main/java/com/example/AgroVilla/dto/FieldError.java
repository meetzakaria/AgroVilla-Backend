package com.example.AgroVilla.dto;

public record FieldError(
        String field,
        String errorCode,
        String errorMessage
) {
}