package com.apartmentrecommendation.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiErrorResponse {

    private String message;
    private Map<String, String> errors = new LinkedHashMap<>();

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(String message) {
        this.message = message;
    }

    public ApiErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
