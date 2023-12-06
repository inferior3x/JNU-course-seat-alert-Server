package com.coco3x.jnu_course_seat_alert.config.apiresponse;


import org.springframework.http.HttpStatus;

public class ApiResponseCreator {
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, data, HttpStatus.OK.value());
    }
    public static <T> ApiResponse<T> success(T data, int status){
        return new ApiResponse<>(true, data, status);
    }
    public static ApiResponse<String> fail(String message){
        return new ApiResponse<>(false, message, HttpStatus.BAD_REQUEST.value());
    }
    public static ApiResponse<String> fail(String message, int status){
        return new ApiResponse<>(false, message, status);
    }
}
