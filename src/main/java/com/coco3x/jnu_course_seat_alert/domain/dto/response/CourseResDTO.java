package com.coco3x.jnu_course_seat_alert.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseResDTO {
    private String code;
    private String name;
    private boolean alerted;
    @Builder
    public CourseResDTO(String code, String name, boolean alerted) {
        this.code = code;
        this.name = name;
        this.alerted = alerted;
    }
}
