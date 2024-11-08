package org.lms.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.lms.exception.BadRequestException;
import org.lms.mapper.ErrorMapper;
import org.lms.utils.UUIDUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookRequestDTO {

    @JsonProperty("userId")
    private String customerId;
    private String bookId;

    @JsonIgnore
    private Action action;

    public BookRequestDTO(BookRequestDTO dto, Action action) {
        this.customerId = dto.getCustomerId();
        this.bookId = dto.getBookId();
        this.action = action;
    }

    public BookRequestDTO(String customerId) {
        this.customerId = customerId;
        this.bookId = null;
        this.action = null;
    }


    public enum Action {
        BORROW,
        RETURN;
    }

    public void validateRequest() {
        validateCustomerId();
        validateBookId();
    }

    public void validateCustomerId() {
        if (customerId == null) {
            throw new BadRequestException(ErrorMapper.USER_ID_MUST_NOT_BE_NULL);
        }
        if (!UUIDUtils.isValid(customerId)) {
            throw new BadRequestException(ErrorMapper.USER_ID_NOT_VALID);
        }
    }

    public void validateBookId() {
        if (bookId == null) {
            throw new BadRequestException(ErrorMapper.BOOK_ID_MUST_NOT_BE_NULL);
        }
        if (!UUIDUtils.isValid(bookId)) {
            throw new BadRequestException(ErrorMapper.BOOK_ID_NOT_VALID);
        }
    }
}
