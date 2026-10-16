package com.zaid.recruitment.Exception;

public class ExceptionMessages {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ExceptionMessages(String message) {
        this.message = message;
    }
}
