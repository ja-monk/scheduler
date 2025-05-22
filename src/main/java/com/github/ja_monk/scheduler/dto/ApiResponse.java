package com.github.ja_monk.scheduler.dto;

import java.time.Instant;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
    private Instant timestamp;

    // cosntructor
    public ApiResponse(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now();
    }

    // ok method that constructs a successful response
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, data, null);
    }

    // error method that constructs a failure response
    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<T>(false, null, errorMessage);
    }

    // getters, required for jackson to serialise to JSON
    public boolean getSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getError() {
        return this.error;
    }
    
    public Instant getTimestamp() {
        return this.timestamp;
    }
}
