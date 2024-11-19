package com.onetry.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // User Exception
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다."),
    USER_DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    // File Exception
    FILE_WRITE_ERROR(HttpStatus.BAD_REQUEST, "파일을 쓰던 중 문제가 발생했습니다."),
    FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST, "파일을 시스템에서 삭제하던 중 문제가 발생했습니다."),
    FILE_READ_ERROR(HttpStatus.BAD_REQUEST, "파일을 시스템에서 읽어오던 중 문제가 발생했습니다."),
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),
    // UserPreference Exception
    USER_PREFERENCE_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 유저 선호 데이터가 존재하지 않습니다."),

    // Certification Exception
    UNAUTHORIZED_ACCESS_CERTIFICATION(HttpStatus.BAD_REQUEST, "해당 자격증을 조회할 권한이 없습니다."),
    CERTIFICATION_NOT_EXIST(HttpStatus.BAD_REQUEST,"해당 자격증이 존재하지 않습니다."),

    // Portfolio Exception
    PORTFOLIO_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 포트폴리오가 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS_PORTFOLIO(HttpStatus.BAD_REQUEST,"해당 포트폴리오를 조회할 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message){
        this.status =status;
        this.message = message;
    }
}