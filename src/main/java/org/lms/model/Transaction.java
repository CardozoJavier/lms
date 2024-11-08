package org.lms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    private UUID id;
    private Type type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID customerId;
    private UUID bookId;
    private Book book;

    public enum Type {
        BORROW,
        RETURN
    }
}
