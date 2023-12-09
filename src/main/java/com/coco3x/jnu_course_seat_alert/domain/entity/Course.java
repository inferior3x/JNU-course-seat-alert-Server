package com.coco3x.jnu_course_seat_alert.domain.entity;

import com.coco3x.jnu_course_seat_alert.domain.dto.response.CourseResDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String grade;
    @Column(nullable = false)
    private boolean isSelfAlerted;
    @Column(nullable = false)
    private boolean isOtherAlerted;

    @Builder
    public Course(String code, String name, String grade) {
        this.code = code;
        this.name = name;
        this.grade = grade;
        this.isSelfAlerted = false;
        this.isOtherAlerted = false;
    }

    public CourseResDTO toDTO(int courseType){
        boolean alerted = (courseType == 0) ? isSelfAlerted : isOtherAlerted;
        return CourseResDTO.builder()
                .name(name)
                .code(code)
                .alerted(alerted)
                .build();
    }

}