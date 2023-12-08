package com.coco3x.jnu_course_seat_alert.service;

import com.coco3x.jnu_course_seat_alert.domain.dto.layer.request.CourseCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public Course findCourseByCode(String code){
        return courseRepository.findCourseByCode(code).orElse(null);
    }

    @Transactional
    public void createCourse(CourseCreateReqDTO courseCreateReqDTO){
        //크롤러에게 유효한 강의인지 검증 요청
        //유효하면 등록
    }

}
