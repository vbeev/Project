package com.project.summoners_beta.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughCoinsException extends RuntimeException {

    public NotEnoughCoinsException() {
        super("Not enough coins!");
    }
}
