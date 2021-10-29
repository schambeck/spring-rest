package com.jobsity.rest.base;

public class ErrorData {

    private final String message;

    public ErrorData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
