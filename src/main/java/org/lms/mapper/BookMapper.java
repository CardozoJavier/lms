package org.lms.mapper;

import org.lms.dto.response.BookListResponseDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.model.Book;
import org.lms.utils.PaginationCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "id", target = "bookId")
    @Mapping(target = "title")
    @Mapping(target = "category")
    @Mapping(target = "genre")
    @Mapping(target = "author")
    @Mapping(target = "language")
    @Mapping(target = "borrowedAt")
    @Mapping(target = "returnedAt")
    BookResponseDTO toDTO(Book book);

    List<BookListResponseDTO.Data> bookListToDataList(List<Book> books);

    default BookListResponseDTO toListDTO(Long booksAvailable, List<Book> allBooks, int pageSize, int currentPage) {
        BookListResponseDTO responseDTO = new BookListResponseDTO();

        responseDTO.setData(bookListToDataList(allBooks));

        PaginationCalculator calculator = new PaginationCalculator();
        BookListResponseDTO.Pagination pagination = calculator.calculate(booksAvailable, pageSize, currentPage);
        responseDTO.setPagination(pagination);
        responseDTO.setSuccess(true);
        responseDTO.setTimestamp(LocalDateTime.now());

        return responseDTO;
    }
}
