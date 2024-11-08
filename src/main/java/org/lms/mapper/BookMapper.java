package org.lms.mapper;

import org.lms.dto.response.BookResponseDTO;
import org.lms.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}
