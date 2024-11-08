package org.lms.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private UUID id;
    private String title;
    private String category;
    private String genre;
    private String author;
    private String language;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private Boolean available;
    private UUID customerId;

    public boolean isAvailable() {
        return available;
    }
}
