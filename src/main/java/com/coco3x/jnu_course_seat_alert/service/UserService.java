package com.coco3x.jnu_course_seat_alert.service;

import com.coco3x.jnu_course_seat_alert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
