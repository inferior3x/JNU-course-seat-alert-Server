package com.coco3x.jnu_course_seat_alert.controller;

import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponse;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponseCreator;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    //유효하지 않은 body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return ApiResponseCreator.fail("유효하지 않은 요청입니다.");
    }
    //@Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        if (e.getBindingResult().getFieldError() != null)
            return ApiResponseCreator.fail(e.getBindingResult().getFieldError().getDefaultMessage());
        else
            return ApiResponseCreator.fail("유효하지 않은 입력입니다.");
    }
    //session 없을 때
    @ExceptionHandler(IllegalAccessException.class)
    public ApiResponse<?> handleIllegalAccessException(IllegalAccessException e){
        return ApiResponseCreator.fail(e.getMessage());
    }
}

