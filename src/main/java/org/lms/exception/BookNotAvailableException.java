package org.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String message) {
        super(message);
    }
}
