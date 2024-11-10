package org.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookListResponseDTO extends BaseResponseDTO {

    private List<Data> data;
    private Pagination pagination;

    public BookListResponseDTO(List<Data> data, Pagination pagination, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.data = data;
        this.pagination = pagination;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {

        private UUID bookId;
        private String title;
        private String category;
        private String genre;
        private String author;
        private String language;
        private LocalDateTime borrowedAt;
        private LocalDateTime returnedAt;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Pagination {

        private Integer currentPage;
        private Boolean hasNext;
        private Boolean hasPrevious;
        private Integer nextPage;
        private Integer pageSize;
        private Long totalItems;
        private Integer totalPages;
    }
}
