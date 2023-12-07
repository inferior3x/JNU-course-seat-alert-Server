package com.coco3x.jnu_course_seat_alert.controller;

import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiMessage;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponse;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponseCreator;
import com.coco3x.jnu_course_seat_alert.config.constant.SessionConst;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.LogInReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.SignUpReqDTO;
import com.coco3x.jnu_course_seat_alert.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody @Valid SignUpReqDTO signUpReqDTO){
        userService.signup(signUpReqDTO);
        return ApiResponseCreator.success(new ApiMessage("회원가입 완료"));
    }

    @PostMapping("/login")
    public ApiResponse<?> login(HttpSession session, @RequestBody @Valid LogInReqDTO logInReqDTO){
        Long id = userService.login(logInReqDTO);
        session.setAttribute(SessionConst.ID, id);
        session.setAttribute(SessionConst.AUTHORIZATION, true);
        return ApiResponseCreator.success(new ApiMessage("로그인 완료"));
    }

    @PostMapping("/example")
    public ApiResponse<?> example(){
        return ApiResponseCreator.success(new ApiMessage("success"));
    }

}
