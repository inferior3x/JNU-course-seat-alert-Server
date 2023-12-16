package com.coco3x.jnu_course_seat_alert.interceptor;

import com.coco3x.jnu_course_seat_alert.config.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

public class AuthenticationCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Optional.ofNullable(session.getAttribute(SessionConst.AUTHENTICATION))
                .orElseThrow(() -> new IllegalAccessException("로그인이 필요합니다."));
        return true;
    }
}
