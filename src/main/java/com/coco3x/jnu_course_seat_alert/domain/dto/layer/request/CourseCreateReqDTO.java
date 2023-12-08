package com.coco3x.jnu_course_seat_alert.domain.dto.layer.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseCreateReqDTO {
    private String code;
    private String name;
    private int grade;

    @Builder
    public CourseCreateReqDTO(String code, String name, int grade) {
        this.code = code;
        this.name = name;
        this.grade = grade;
    }
}
