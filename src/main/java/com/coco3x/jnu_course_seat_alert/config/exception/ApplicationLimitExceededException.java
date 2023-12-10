package com.coco3x.jnu_course_seat_alert.config.exception;

public class ApplicationLimitExceededException extends RuntimeException{
    public ApplicationLimitExceededException(String message){
        super(message);
    }
}
