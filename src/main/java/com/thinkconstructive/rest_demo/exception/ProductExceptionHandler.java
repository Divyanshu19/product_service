package com.thinkconstructive.rest_demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(value = {ProductNotFoundException.class })
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException productNotFoundException)
    {
      ProductException productException=new ProductException(
              productNotFoundException.getMessage(), productNotFoundException.getCause(),
              HttpStatus.NOT_FOUND
      );
      return new ResponseEntity<>(productException,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
   public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
       Map<String,String> errors=new HashMap<>();
       ex.getBindingResult().getAllErrors().forEach(error->{
           String fieldName = ((FieldError) error).getField();
           String defaultMessage = error.getDefaultMessage();
           errors.put(fieldName,defaultMessage);

       });
       return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);

    }
}
