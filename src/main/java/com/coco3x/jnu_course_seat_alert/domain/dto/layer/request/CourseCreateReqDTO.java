package com.coco3x.jnu_course_seat_alert.domain.dto.layer.request;

import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseCreateReqDTO {
    private String code;
    private String name;
    private String grade;

    @Builder
    public CourseCreateReqDTO(String code, String name, String grade) {
        this.code = code;
        this.name = name;
        this.grade = grade;
    }
    public Course toEntity(){
        return Course.builder()
                .code(code)
                .name(name)
                .grade(grade)
                .build();
    }
}
