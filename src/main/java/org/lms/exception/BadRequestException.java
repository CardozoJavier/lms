package org.lms.exception;

import lombok.Getter;
import org.lms.mapper.ErrorMapper;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private final int code;

    public BadRequestException(ErrorMapper error) {
        super(error.getMessage());
        this.code = error.getCode();
    }
}
