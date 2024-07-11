package org.landvibe.ass1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public BookException(ErrorMessage errorMessage) {
        this.message = errorMessage.message;
        this.httpStatus = errorMessage.httpStatus;
    }
}
