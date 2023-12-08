package com.coco3x.jnu_course_seat_alert.repository;

import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserById(Long id);

    boolean existsByUserId(String userId);
}
