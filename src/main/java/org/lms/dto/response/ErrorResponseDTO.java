package org.lms.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO extends BaseResponseDTO {

    private int code;
    private String message;

    public ErrorResponseDTO(int code, String message, LocalDateTime timestamp) {
        super(false, timestamp);
        this.code = code;
        this.message = message;
    }
}
