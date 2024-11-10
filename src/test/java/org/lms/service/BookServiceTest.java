package org.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lms.dto.request.BookListRequestDTO;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookListResponseDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.exception.BookNotAvailableException;
import org.lms.exception.NotFoundException;
import org.lms.mapper.BookMapper;
import org.lms.model.Book;
import org.lms.model.Transaction;
import org.lms.repository.BookRepository;
import org.lms.repository.CustomerRepository;
import org.lms.repository.TransactionRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookService bookService;

    private UUID sampleBookId;
    private UUID sampleCustomerId;
    private BookRequestDTO bookRequestDTO;

    @BeforeEach
    void setUp() {
        sampleBookId = UUID.randomUUID();
        sampleCustomerId = UUID.randomUUID();
        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setBookId(sampleBookId.toString());
        bookRequestDTO.setCustomerId(sampleCustomerId.toString());
        bookRequestDTO.setAction(BookRequestDTO.Action.BORROW);
        // Setup bookRequestDTO properties
    }

    @Test
    void testGetAllAvailable() {
        // Given
        List<Book> books = Collections.singletonList(new Book());
        BookListResponseDTO bookListResponseDTO = BookListResponseDTO.builder()
                .data(Collections.emptyList())
                .build();
        long totalElements = books.size();

        when(bookRepository.countAllByAvailableTrue()).thenReturn(totalElements);
        when(bookRepository.findAllByAvailableTrue(anyLong(), anyInt(), anyInt())).thenReturn(books);

        when(mapper.toListDTO(totalElements, books, PAGE_SIZE, PAGE_NUMBER)).thenReturn(bookListResponseDTO);

        BookListRequestDTO bookListRequestDTO = new BookListRequestDTO(PAGE_NUMBER, PAGE_SIZE);

        // When
        Optional<BookListResponseDTO> result = bookService.getAllAvailable(bookListRequestDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals(bookListResponseDTO, result.get());
        verify(bookRepository, times(1)).findAllByAvailableTrue(anyLong(), anyInt(), anyInt());
    }

    @Test
    void testBorrowBookSuccessfully() {
        // Given
        Book book = new Book();
        book.setAvailable(true);
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);
        when(bookRepository.updateById(any(), any(), anyBoolean(), any(), any())).thenReturn(Optional.of(book));
        when(mapper.toDTO(book)).thenReturn(new BookResponseDTO());

        // When
        Optional<BookResponseDTO> result = bookService.borrow(bookRequestDTO);

        // Then
        assertTrue(result.isPresent());
        verify(bookRepository, times(1)).findById(sampleBookId);
        verify(transactionRepository, times(1)).insertTransaction(any(Transaction.Type.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void testBorrowBookNotAvailable() {
        // Given
        Book book = new Book();
        book.setAvailable(false);
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);

        // When
        assertThrows(BookNotAvailableException.class, () -> bookService.borrow(bookRequestDTO));

        // Then
        verify(bookRepository, times(1)).findById(sampleBookId);
    }

    @Test
    void testReturnBookSuccessfully() {
        // Given
        Book book = new Book();
        book.setAvailable(false);

        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);
        when(bookRepository.updateById(any(), any(), anyBoolean(), any(), any())).thenReturn(Optional.of(book));

        when(mapper.toDTO(book)).thenReturn(new BookResponseDTO());

        // When
        Optional<BookResponseDTO> result = bookService.returnBook(bookRequestDTO);

        // Then
        assertTrue(result.isPresent());
        verify(bookRepository, times(1)).findById(sampleBookId);
        verify(transactionRepository, times(1)).insertTransaction(any(Transaction.Type.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void testReturnBookNotFound() {
        // Given
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.empty());
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);

        // When
        assertThrows(NotFoundException.class, () -> bookService.returnBook(bookRequestDTO));

        // Then
        verify(bookRepository, times(1)).findById(sampleBookId);
    }

    @Test
    void getAllAvailable_WhenBooksExist_ReturnsBookList() {
        // Given
        BookListRequestDTO request = new BookListRequestDTO(PAGE_NUMBER, PAGE_SIZE);
        List<Book> books = List.of(new Book(), new Book());
        long totalBooks = 2L;
        BookListResponseDTO expectedResponse = new BookListResponseDTO();
        expectedResponse.setData(books.stream()
                .map(book -> BookListResponseDTO.Data.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build())
                .collect(Collectors.toList()));

        when(bookRepository.countAllByAvailableTrue()).thenReturn(totalBooks);
        when(bookRepository.findAllByAvailableTrue(totalBooks, PAGE_SIZE, PAGE_NUMBER))
                .thenReturn(books);
        when(mapper.toListDTO(totalBooks, books, PAGE_SIZE, PAGE_NUMBER))
                .thenReturn(expectedResponse);

        // When
        Optional<BookListResponseDTO> result = bookService.getAllAvailable(request);

        // Then
        assertTrue(result.isPresent());
        assertSame(expectedResponse, result.get());
        verify(bookRepository).countAllByAvailableTrue();
        verify(bookRepository).findAllByAvailableTrue(totalBooks, PAGE_SIZE, PAGE_NUMBER);
        verify(mapper).toListDTO(totalBooks, books, PAGE_SIZE, PAGE_NUMBER);
    }

    @Test
    void getAllAvailable_WhenNoBooks_ReturnsEmpty() {
        // Given
        BookListRequestDTO request = new BookListRequestDTO(PAGE_NUMBER, PAGE_SIZE);
        when(bookRepository.countAllByAvailableTrue()).thenReturn(0L);

        // When
        Optional<BookListResponseDTO> result = bookService.getAllAvailable(request);

        // Then
        assertFalse(result.isPresent());
        verify(bookRepository).countAllByAvailableTrue();
        verify(bookRepository, never()).findAllByAvailableTrue(anyLong(), anyInt(), anyInt());
        verify(mapper, never()).toListDTO(anyLong(), anyList(), anyInt(), anyInt());
    }

    @Test
    void getAllAvailable_WithDifferentPageSize_ReturnsCorrectResults() {
        // Given
        int customPageSize = 5;
        BookListRequestDTO request = new BookListRequestDTO(PAGE_NUMBER, customPageSize);
        List<Book> books = List.of(new Book());
        long totalBooks = 1L;
        BookListResponseDTO expectedResponse = new BookListResponseDTO();
        expectedResponse.setData(books.stream()
                .map(book -> BookListResponseDTO.Data.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build())
                .collect(Collectors.toList()));

        when(bookRepository.countAllByAvailableTrue()).thenReturn(totalBooks);
        when(bookRepository.findAllByAvailableTrue(totalBooks, customPageSize, PAGE_NUMBER))
                .thenReturn(books);
        when(mapper.toListDTO(totalBooks, books, customPageSize, PAGE_NUMBER))
                .thenReturn(expectedResponse);

        // When
        Optional<BookListResponseDTO> result = bookService.getAllAvailable(request);

        // Then
        assertTrue(result.isPresent());
        assertSame(expectedResponse, result.get());
        verify(bookRepository).findAllByAvailableTrue(totalBooks, customPageSize, PAGE_NUMBER);
    }

    @Test
    void getAllAvailable_WithDifferentPageNumber_ReturnsCorrectResults() {
        // Given
        int customPageNumber = 2;
        BookListRequestDTO request = new BookListRequestDTO(customPageNumber, PAGE_SIZE);
        List<Book> books = List.of(new Book());
        long totalBooks = 1L;
        BookListResponseDTO expectedResponse = new BookListResponseDTO();
        expectedResponse.setData(books.stream()
                .map(book -> BookListResponseDTO.Data.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build())
                .collect(Collectors.toList()));

        when(bookRepository.countAllByAvailableTrue()).thenReturn(totalBooks);
        when(bookRepository.findAllByAvailableTrue(totalBooks, PAGE_SIZE, customPageNumber))
                .thenReturn(books);
        when(mapper.toListDTO(totalBooks, books, PAGE_SIZE, customPageNumber))
                .thenReturn(expectedResponse);

        // When
        Optional<BookListResponseDTO> result = bookService.getAllAvailable(request);

        // Then
        assertTrue(result.isPresent());
        assertSame(expectedResponse, result.get());
        verify(bookRepository).findAllByAvailableTrue(totalBooks, PAGE_SIZE, customPageNumber);
    }

    @Test
    void getAllAvailable_WhenValidateRequestThrowsException_PropagatesException() {
        // Given
        BookListRequestDTO request = mock(BookListRequestDTO.class);
        doThrow(new IllegalArgumentException("Invalid request")).when(request).validateRequest();

        // When // Then
        assertThrows(IllegalArgumentException.class, () -> bookService.getAllAvailable(request));
        verify(bookRepository, never()).countAllByAvailableTrue();
    }

    @Test
    void getAllAvailable_WhenRepositoryThrowsException_PropagatesException() {
        // Given
        BookListRequestDTO request = new BookListRequestDTO(PAGE_NUMBER, PAGE_SIZE);
        when(bookRepository.countAllByAvailableTrue())
                .thenThrow(new RuntimeException("Database error"));

        // When // Then
        assertThrows(RuntimeException.class, () -> bookService.getAllAvailable(request));
        verify(bookRepository).countAllByAvailableTrue();
        verify(bookRepository, never()).findAllByAvailableTrue(anyLong(), anyInt(), anyInt());
    }

}