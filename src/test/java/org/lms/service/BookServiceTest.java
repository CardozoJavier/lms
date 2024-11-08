package org.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lms.dto.request.BookRequestDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

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
        List<Book> books = Collections.singletonList(new Book());
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        List<BookResponseDTO> bookResponseDTOs = Collections.singletonList(bookResponseDTO);

        when(bookRepository.findAllByAvailableTrue()).thenReturn(books);

        when(mapper.toDTO(any(Book.class))).thenReturn(bookResponseDTO);
        Optional<List<BookResponseDTO>> result = bookService.getAllAvailable();

        assertTrue(result.isPresent());
        assertEquals(bookResponseDTOs, result.get());
        verify(bookRepository, times(1)).findAllByAvailableTrue();
    }

    @Test
    void testBorrowBookSuccessfully() {
        Book book = new Book();
        book.setAvailable(true);
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);
        when(bookRepository.updateById(any(), any(), anyBoolean(), any(), any())).thenReturn(Optional.of(book));
        when(mapper.toDTO(book)).thenReturn(new BookResponseDTO());

        Optional<BookResponseDTO> result = bookService.borrow(bookRequestDTO);

        assertTrue(result.isPresent());
        verify(bookRepository, times(1)).findById(sampleBookId);
        verify(transactionRepository, times(1)).insertTransaction(any(Transaction.Type.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void testBorrowBookNotAvailable() {
        Book book = new Book();
        book.setAvailable(false);
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);

        assertThrows(BookNotAvailableException.class, () -> bookService.borrow(bookRequestDTO));

        verify(bookRepository, times(1)).findById(sampleBookId);
    }

    @Test
    void testReturnBookSuccessfully() {
        Book book = new Book();
        book.setAvailable(false);

        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.of(book));
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);
        when(bookRepository.updateById(any(), any(), anyBoolean(), any(), any())).thenReturn(Optional.of(book));

        when(mapper.toDTO(book)).thenReturn(new BookResponseDTO());

        Optional<BookResponseDTO> result = bookService.returnBook(bookRequestDTO);

        assertTrue(result.isPresent());
        verify(bookRepository, times(1)).findById(sampleBookId);
        verify(transactionRepository, times(1)).insertTransaction(any(Transaction.Type.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void testReturnBookNotFound() {
        when(bookRepository.findById(sampleBookId)).thenReturn(Optional.empty());
        when(customerRepository.existsById(sampleCustomerId)).thenReturn(true);

        assertThrows(NotFoundException.class, () -> bookService.returnBook(bookRequestDTO));

        verify(bookRepository, times(1)).findById(sampleBookId);
    }
}