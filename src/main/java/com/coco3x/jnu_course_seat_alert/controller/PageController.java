package com.coco3x.jnu_course_seat_alert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    //회원가입 페이지
    @GetMapping("/signup")
    public void signup(){}
    //로그인 페이지
    @GetMapping("/login")
    public void login(){}
    //강의 페이지
    @GetMapping("/course")
    public void course(){}
    //설정 페이지
    @GetMapping("/setting")
    public void setting(){}
}
