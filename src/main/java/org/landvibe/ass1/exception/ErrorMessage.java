package org.landvibe.ass1.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    INVALID_TITLE("제목을 입력하세요.", HttpStatus.BAD_REQUEST),
    INVALID_ID("존재하지 않는 아이디입니다.", HttpStatus.BAD_REQUEST);

    public HttpStatus httpStatus;
    public String message;

    ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}