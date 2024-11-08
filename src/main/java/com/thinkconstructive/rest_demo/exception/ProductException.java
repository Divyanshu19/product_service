package com.thinkconstructive.rest_demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProductException {
    private final String message;

}
