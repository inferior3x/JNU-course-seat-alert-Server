package com.coco3x.jnu_course_seat_alert.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true) //false 로 바꾸기
    private String pushNotificationId;

    @Builder
    public User(String userId, String password, String pushNotificationId){
        this.userId = userId;
        this.password = password;
        this.pushNotificationId = pushNotificationId;
    }

    public void updatePassword(String hashPassword){
        this.password = hashPassword;
    }
}
