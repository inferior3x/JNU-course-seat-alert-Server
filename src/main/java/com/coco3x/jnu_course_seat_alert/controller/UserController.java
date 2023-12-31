package com.coco3x.jnu_course_seat_alert.controller;

import com.coco3x.jnu_course_seat_alert.config.annotation.Session;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiMessage;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponse;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponseCreator;
import com.coco3x.jnu_course_seat_alert.config.constant.SessionConst;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.UserLogInReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.UserSignUpReqDTO;
import com.coco3x.jnu_course_seat_alert.service.ApplicantService;
import com.coco3x.jnu_course_seat_alert.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/authentication")
    public ApiResponse<?> checkAuthorization(@Session(attr = SessionConst.AUTHENTICATION) Boolean authentication){
        return ApiResponseCreator.success(authentication);
    }

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody @Valid UserSignUpReqDTO userSignUpReqDTO){
        userService.signup(userSignUpReqDTO);
        return ApiResponseCreator.success(new ApiMessage("회원가입 완료"));
    }

    @PostMapping("/login")
    public ApiResponse<?> login(HttpSession session, @RequestBody @Valid UserLogInReqDTO userLogInReqDTO){
        Long id = userService.login(userLogInReqDTO);
        session.setAttribute(SessionConst.ID, id);
        session.setAttribute(SessionConst.AUTHENTICATION, true);
        return ApiResponseCreator.success(new ApiMessage("로그인 완료"));
    }

    @PostMapping("logout")
    public ApiResponse<?> logout(HttpSession session, @Session(attr = "id") Long id){
        session.removeAttribute(SessionConst.ID);
        session.removeAttribute(SessionConst.AUTHENTICATION);
        return ApiResponseCreator.success(new ApiMessage("로그아웃 완료"));
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<?> withdraw(HttpSession session, @Session(attr = "id") Long id){
        session.removeAttribute(SessionConst.ID);
        session.removeAttribute(SessionConst.AUTHENTICATION);
        userService.withdraw(id);
        return ApiResponseCreator.success(new ApiMessage("탈퇴 완료"));
    }


}
