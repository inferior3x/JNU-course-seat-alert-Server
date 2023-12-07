package com.coco3x.jnu_course_seat_alert.domain.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LogInReqDTO {
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}