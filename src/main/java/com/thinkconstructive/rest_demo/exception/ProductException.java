package com.thinkconstructive.rest_demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ProductException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

}
