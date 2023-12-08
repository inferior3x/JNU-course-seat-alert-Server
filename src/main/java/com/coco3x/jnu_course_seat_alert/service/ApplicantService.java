package com.coco3x.jnu_course_seat_alert.service;

import com.coco3x.jnu_course_seat_alert.domain.dto.request.ApplicantCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.Applicant;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.coco3x.jnu_course_seat_alert.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    @Transactional
    public void deleteApplicantFromCourse(User user, Course course){
        applicantRepository.deleteApplicantByUserAndCourse(user, course);
    }

    public void enrollApplicantInCourse(int courseType, User user, Course course){
        Applicant applicant = Applicant.builder()
                .courseType(courseType)
                .course(course)
                .user(user)
                .build();
        applicantRepository.save(applicant);
    }

}
