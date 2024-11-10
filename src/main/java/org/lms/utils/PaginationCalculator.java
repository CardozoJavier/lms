package org.lms.utils;

import org.lms.dto.response.BookListResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PaginationCalculator {

    public BookListResponseDTO.Pagination calculate(Long totalItems, int pageSize, int currentPage) {
        BookListResponseDTO.Pagination pagination = new BookListResponseDTO.Pagination();

        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        pagination.setCurrentPage(currentPage);
        pagination.setPageSize(pageSize);
        pagination.setTotalItems(totalItems);
        pagination.setTotalPages(totalPages);

        boolean hasNext = currentPage < (totalPages - 1);
        pagination.setHasNext(hasNext);
        pagination.setNextPage(hasNext ? currentPage + 1 : null);

        pagination.setHasPrevious(currentPage > 0);

        return pagination;
    }
}