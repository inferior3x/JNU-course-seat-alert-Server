package com.coco3x.jnu_course_seat_alert.repository;

import com.coco3x.jnu_course_seat_alert.domain.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}
