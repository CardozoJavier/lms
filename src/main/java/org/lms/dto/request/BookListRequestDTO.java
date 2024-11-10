package org.lms.dto.request;

import lombok.*;
import org.lms.exception.BadRequestException;
import org.lms.mapper.ErrorMapper;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookListRequestDTO {

    private Integer pageNumber = 0;
    private Integer pageSize = 10;

    public void validateRequest() {
        if (pageNumber < 0) {
            throw new BadRequestException(ErrorMapper.INVALID_PAGE_NUMBER);
        }
        if (pageSize < 1) {
            throw new BadRequestException(ErrorMapper.INVALID_PAGE_SIZE);
        }
    }
}
