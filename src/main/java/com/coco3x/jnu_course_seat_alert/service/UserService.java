package com.coco3x.jnu_course_seat_alert.service;

import com.coco3x.jnu_course_seat_alert.domain.dto.request.LogInReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.SignUpReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.coco3x.jnu_course_seat_alert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignUpReqDTO signUpReqDTO){
        if (userRepository.existsByUserId(signUpReqDTO.getUserId()))
            throw new IllegalArgumentException("이미 있는 회원입니다.");
        User user = signUpReqDTO.toEntity();
        userRepository.save(user);
    }
}
