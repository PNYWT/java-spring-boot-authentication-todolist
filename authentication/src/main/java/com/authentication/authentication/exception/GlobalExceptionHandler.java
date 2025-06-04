package com.authentication.authentication.exception;

import com.authentication.authentication.dto.BaseResponseDto;
import com.authentication.authentication.exception.components.InvalidParameterException;
import com.authentication.authentication.exception.components.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<BaseResponseDto<String>> handleTaskNotFound(TaskNotFoundException ex) {
        BaseResponseDto error = new BaseResponseDto<String>()
                .setBaseResponse("102", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParam(InvalidParameterException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("ret", "400");
        error.put("message", "Unexpected parameter: " + ex.getInvalidParam());
        error.put("suggest", "Did you mean: " + ex.getSuggestedParam() + "?");

        return ResponseEntity.badRequest().body(error);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
