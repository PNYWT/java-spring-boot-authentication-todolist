package com.authentication.authentication.exception;

import com.authentication.authentication.dto.BaseResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ดัก validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        BaseResponseDto<Map<String, String>> response = new BaseResponseDto<Map<String, String>>()
                .setBaseReposeData("102", "Validation Failed", errors);
        /*
         FieldError firstError = ex.getBindingResult().getFieldErrors().get(0);

    BaseResponseDto<Void> response = new BaseResponseDto<Void>()
            .setBaseReposeData("102", firstError.getDefaultMessage(), null);
         */
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ดัก IllegalArgumentException แล้วตอบในรูปแบบ BaseResponseDto
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        BaseResponseDto response = new BaseResponseDto<Void>().setBaseResponse("102", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Optional: ดัก BadCredentialsException (401)
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(org.springframework.security.authentication.BadCredentialsException ex) {
        BaseResponseDto<Void> response = new BaseResponseDto<Void>()
                .setBaseReposeData("401", "Please Login.", null);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Optional: ดัก RuntimeException ที่ไม่รู้สาเหตุอื่น ๆ
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGenericRuntimeException(RuntimeException ex) {
        BaseResponseDto<Void> response = new BaseResponseDto<Void>()
                .setBaseReposeData("500", "Unexpected error: " + ex.getMessage(), null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

