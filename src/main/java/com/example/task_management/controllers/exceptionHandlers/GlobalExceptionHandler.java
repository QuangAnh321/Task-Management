package com.example.task_management.controllers.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.example.task_management.controllers.response.BasicResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public BasicResponse handleNotFoundException(EntityNotFoundException ex) {
        return new BasicResponse(false, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SecurityException.class)
    public BasicResponse handleForbiddenException(Forbidden ex) {
        return new BasicResponse(false, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BasicResponse handleGenericException(Exception ex) {
        return new BasicResponse(false, "An unexpected error occurred: " + ex.getMessage());
    }
}
