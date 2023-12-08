package com.coco3x.jnu_course_seat_alert.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplicantCreateReqDTO {
    @NotBlank(message = "Invalid code")
    private String code;
    @NotBlank(message = "Invalid name")
    private String name;
    @NotBlank(message = "Invalid grade")
    private String grade;
    @NotNull(message = "Invalid course type")
    private Integer courseType;
}
