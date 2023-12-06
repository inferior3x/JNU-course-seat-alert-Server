package com.coco3x.jnu_course_seat_alert.domain.dto.request;

import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpReqDTO {
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 4~12글자의 영어, 숫자여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=-]{4,12}$", message = "비밀번호는 4~12글자의 영어, 숫자, 특수문자여야 합니다.")
    private String password;

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}