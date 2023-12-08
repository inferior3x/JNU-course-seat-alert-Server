package com.coco3x.jnu_course_seat_alert.domain.dto.request;

import lombok.Getter;

@Getter
public class ApplicantCreateReqDTO {
    private String code;
    private String name;
    private int grade;
    private int courseType;
}
