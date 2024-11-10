package org.lms.mapper;

import lombok.Getter;

@Getter
public enum ErrorMapper {

    BOOK_NOT_FOUND(90001, "Book not found"),
    CUSTOMER_NOT_FOUND(90002, "Customer not found"),
    BOOK_ALREADY_BORROWED(90003, "Book already borrowed"),
    BOOK_ALREADY_RETURNED(90004, "Book already returned"),
    BOOK_NOT_BORROWED(90005, "Book not borrowed"),
    BOOK_IS_NOT_BORROWED(90006, "Book is not borrowed"),
    BOOK_IS_NOT_AVAILABLE(90007, "Book is not available"),

    BOOK_ID_MUST_NOT_BE_NULL(90008, "`bookId` must not be null"),
    BOOK_ID_NOT_VALID(90009, "`bookId` is not a valid UUID"),
    USER_ID_MUST_NOT_BE_NULL(90010, "`userId` must not be null"),
    USER_ID_NOT_VALID(90011, "`userId` is not a valid UUID"),
    INVALID_PAGE_SIZE(90012, "Invalid page size"),
    INVALID_PAGE_NUMBER(90013, "Invalid page number"),
    INVALID_PAGINATION(90014, "Invalid page size or page number");


    private final int code;
    private final String message;

    ErrorMapper(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
