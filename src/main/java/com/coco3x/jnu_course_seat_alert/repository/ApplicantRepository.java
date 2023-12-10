package com.coco3x.jnu_course_seat_alert.repository;

import com.coco3x.jnu_course_seat_alert.domain.entity.Applicant;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findApplicantsByUser(User user);
    void deleteApplicantByUserAndCourse(User user, Course course);

    long countApplicantsByUser(User user);
}
