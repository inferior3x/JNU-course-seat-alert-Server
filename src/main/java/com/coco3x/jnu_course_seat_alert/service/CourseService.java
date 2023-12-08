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

    public void createCourse(CourseCreateReqDTO courseCreateReqDTO){
        Course course = courseCreateReqDTO.toEntity();
        courseRepository.save(course);
    }

}
