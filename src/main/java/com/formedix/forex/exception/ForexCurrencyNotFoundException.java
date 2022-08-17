package com.formedix.forex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource was not found")
public class ForexCurrencyNotFoundException extends RuntimeException {

    public ForexCurrencyNotFoundException(String message) {
        super(message);
    }
}
