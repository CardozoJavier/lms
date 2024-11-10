package org.lms.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseResponseDTO {

    private boolean success;
    private LocalDateTime timestamp;
}
