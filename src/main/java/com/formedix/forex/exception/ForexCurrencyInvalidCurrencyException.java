package com.formedix.forex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Request")
public class ForexCurrencyInvalidCurrencyException extends RuntimeException {

    public ForexCurrencyInvalidCurrencyException(String message) {
        super(message);
    }
}
