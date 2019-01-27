package com.cschlay.car;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CarResponse {
    private String message;
    private boolean success;


    public ResponseEntity<String> getResponse(HttpStatus statusCode) {
        return new ResponseEntity<>(message + '\n', statusCode);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean succeeded) {
        this.success = succeeded;
    }
}
