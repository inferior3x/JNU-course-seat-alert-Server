package com.coco3x.jnu_course_seat_alert.config.apiresponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record ApiResponse<T>(boolean success, T data, int status) {}
