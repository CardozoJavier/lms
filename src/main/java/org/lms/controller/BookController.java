package org.lms.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lms.dto.request.BookListRequestDTO;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookListResponseDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<BookListResponseDTO> getAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return bookService.getAllAvailable(new BookListRequestDTO(pageNumber, pageSize))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }

    @PostMapping("/borrow")
    public ResponseEntity<BookResponseDTO> borrowBook(@RequestBody BookRequestDTO bookRequestDTO) {
        return bookService.borrow(new BookRequestDTO(bookRequestDTO, BookRequestDTO.Action.BORROW))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }

    @PostMapping("/return")
    public ResponseEntity<BookResponseDTO> returnBook(@RequestBody BookRequestDTO bookRequestDTO) {
        return bookService.returnBook(new BookRequestDTO(bookRequestDTO, BookRequestDTO.Action.RETURN))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }
}
