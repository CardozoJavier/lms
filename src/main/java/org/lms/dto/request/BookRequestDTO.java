package org.lms.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.lms.exception.BadRequestException;
import org.lms.utils.UUIDUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookRequestDTO {

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
            throw new BadRequestException("customerId must not be null");
        }
        if (!UUIDUtils.isValid(customerId)) {
            throw new BadRequestException("customerId is not a valid UUID");
        }
    }

    public void validateBookId() {
        if (bookId == null) {
            throw new BadRequestException("bookId must not be null");
        }
        if (!UUIDUtils.isValid(bookId)) {
            throw new BadRequestException("bookId is not a valid UUID");
        }
    }
}
