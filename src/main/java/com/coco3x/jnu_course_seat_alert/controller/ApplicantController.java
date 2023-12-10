package com.coco3x.jnu_course_seat_alert.controller;

import com.coco3x.jnu_course_seat_alert.config.annotation.Session;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiMessage;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponse;
import com.coco3x.jnu_course_seat_alert.config.apiresponse.ApiResponseCreator;
import com.coco3x.jnu_course_seat_alert.config.constant.CommonConst;
import com.coco3x.jnu_course_seat_alert.config.exception.ApplicationLimitExceededException;
import com.coco3x.jnu_course_seat_alert.domain.dto.layer.request.CourseCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.ApplicantCreateReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.request.ApplicantDeleteReqDTO;
import com.coco3x.jnu_course_seat_alert.domain.dto.response.CourseResDTO;
import com.coco3x.jnu_course_seat_alert.domain.entity.Course;
import com.coco3x.jnu_course_seat_alert.domain.entity.User;
import com.coco3x.jnu_course_seat_alert.service.ApplicantService;
import com.coco3x.jnu_course_seat_alert.service.CourseService;
import com.coco3x.jnu_course_seat_alert.service.UserService;
import com.coco3x.jnu_course_seat_alert.util.CourseValidationCrawler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applicant")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;
    private final CourseService courseService;
    private final UserService userService;
    private final CourseValidationCrawler courseValidationCrawler;

    @PostMapping("/course")
    public ApiResponse<?> enrollApplicantInCourse(@Session(attr = "id") Long id, @RequestBody @Valid ApplicantCreateReqDTO applicantCreateReqDTO){
        User user = userService.findUserById(id);
        if (applicantService.countApplicants(user) >= CommonConst.APPLICATION_LIMIT)
            throw new ApplicationLimitExceededException("신청 가능한 강의 수는 "+ CommonConst.APPLICATION_LIMIT+"개입니다.");

        Course course = courseService.findCourseByCode(applicantCreateReqDTO.getCode());
        if (course == null) {
            String realName = courseValidationCrawler.validate(applicantCreateReqDTO.getCode(), applicantCreateReqDTO.getName(), applicantCreateReqDTO.getGrade());
            courseService.createCourse(CourseCreateReqDTO.builder()
                    .code(applicantCreateReqDTO.getCode())
                    .grade(applicantCreateReqDTO.getGrade())
                    .name(realName)
                    .build());
        }

        course = courseService.findCourseByCode(applicantCreateReqDTO.getCode());
        applicantService.enrollApplicantInCourse(Integer.parseInt(applicantCreateReqDTO.getCourseType()), user, course);

        return ApiResponseCreator.success(new ApiMessage("-"));
    }

    @GetMapping("/courses")
    public ApiResponse<?> findAppliedCourses(@Session(attr = "id") Long id){
        User user = userService.findUserById(id);
        return ApiResponseCreator.success(courseService.findAppliedCourses(user));
    }

    @DeleteMapping("/course")
    public ApiResponse<?> deleteApplicantFromCourse(@Session(attr = "id") Long id, @RequestBody @Valid ApplicantDeleteReqDTO applicantDeleteReqDTO){
        User user = userService.findUserById(id);
        Course course = courseService.findCourseByCode(applicantDeleteReqDTO.getCode());
        if (course == null) throw new IllegalArgumentException("유효하지 않은 강의입니다.");
        applicantService.deleteApplicantFromCourse(user, course);
        return ApiResponseCreator.success(new ApiMessage("삭제 완료"));
    }
}
