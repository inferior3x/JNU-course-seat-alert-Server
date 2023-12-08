package com.coco3x.jnu_course_seat_alert.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ApplicantDeleteReqDTO {
    @NotBlank
    private String code;
}
