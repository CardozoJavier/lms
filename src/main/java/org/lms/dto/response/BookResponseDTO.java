package org.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.lms.model.Book;
import org.lms.model.Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponseDTO {

    private UUID bookId;
    private String title;
    private String category;
    private String genre;
    private String author;
    private String language;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    public BookResponseDTO(Transaction transaction) {
        this.bookId = transaction.getBook().getId();
        this.title = transaction.getBook().getTitle();
        this.category = transaction.getBook().getCategory();
        this.genre = transaction.getBook().getGenre();
        this.author = transaction.getBook().getAuthor();
        this.language = transaction.getBook().getLanguage();
    }

    public void setReturnedAt(Book book, Transaction tx) {
        if (book.getAvailable()) {
            this.setReturnedAt(tx.getCreatedAt());
        }
    }
}
