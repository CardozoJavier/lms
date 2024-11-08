package org.lms.exception;

import lombok.Getter;
import org.lms.mapper.ErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private final int code;

    public NotFoundException(ErrorMapper error) {
        super(error.getMessage());
        this.code = error.getCode();
    }
}
