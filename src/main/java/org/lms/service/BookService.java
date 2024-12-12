package org.lms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lms.dto.request.BookListRequestDTO;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookListResponseDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.exception.BookNotAvailableException;
import org.lms.exception.NotFoundException;
import org.lms.mapper.BookMapper;
import org.lms.mapper.ErrorMapper;
import org.lms.model.Book;
import org.lms.model.Transaction;
import org.lms.repository.BookRepository;
import org.lms.repository.CustomerRepository;
import org.lms.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BookService {
    
    private final TransactionRepository transactionRepository;
    private BookRepository bookRepository;
    private CustomerRepository customerRepository;
    private BookMapper mapper;
    
    public Optional<BookListResponseDTO> getAllAvailable(BookListRequestDTO request) {
        log.info("Request all available books in library, request={}", request);
        request.validateRequest();
        Long totalBooksAvailable = bookRepository.countAllByAvailableTrue();

        if (totalBooksAvailable == 0) {
            log.warn("No books available");
            return Optional.empty();
        }
        log.info("Books available, total={}", totalBooksAvailable);

        List<Book> books = bookRepository.findAllByAvailableTrue(totalBooksAvailable, request.getPageSize(), request.getPageNumber());
        log.info("books found total={}", books.size());

        BookListResponseDTO response = mapper.toListDTO(totalBooksAvailable, books, request.getPageSize(), request.getPageNumber());

        log.info("Successful page with books={}", response.getData().size());
        return Optional.of(response);
    }

    public Optional<BookResponseDTO> borrow(BookRequestDTO bookRequestDTO) {
        log.info("Request received for borrow book, request={}", bookRequestDTO);
        bookRequestDTO.validateRequest();

        UUID customerId = UUID.fromString(bookRequestDTO.getCustomerId());
        validateCustomer(customerId);

        UUID bookId = UUID.fromString(bookRequestDTO.getBookId());
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent() && book.get().isAvailable()) {
            Optional<Book> updated = bookRepository.updateById(bookId, customerId, false, LocalDateTime.now(), null);
            log.info("Book borrowed successfully, book={}", updated.orElse(null));

            transactionRepository.insertTransaction(Transaction.Type.BORROW, customerId, bookId);
            log.info("Transaction of type {} created successfully, customerId={}, bookId={}", Transaction.Type.BORROW, customerId, bookId);

            return updated.map(b -> mapper.toDTO(b));
        }

        if (book.isPresent()) {
            throw new BookNotAvailableException(ErrorMapper.BOOK_IS_NOT_AVAILABLE);
        }
        log.warn("Book with id={} not found", bookId);
        throw new NotFoundException(ErrorMapper.BOOK_NOT_FOUND);
    }

    public Optional<BookResponseDTO> returnBook(BookRequestDTO bookRequestDTO) {
        log.info("Request received for return book, request={}", bookRequestDTO);
        bookRequestDTO.validateRequest();

        UUID customerId = UUID.fromString(bookRequestDTO.getCustomerId());
        validateCustomer(customerId);

        UUID bookId = UUID.fromString(bookRequestDTO.getBookId());
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent() && !book.get().isAvailable()) {
            Optional<Book> updated = bookRepository.updateById(bookId, customerId, true, book.get().getBorrowedAt(), LocalDateTime.now());
            log.info("Book returned successfully, book={}", updated.orElse(null));

            transactionRepository.insertTransaction(Transaction.Type.RETURN, customerId, bookId);
            log.info("Transaction of type {} created successfully, customerId={}, bookId={}", Transaction.Type.RETURN, customerId, bookId);

            return updated.map(b -> mapper.toDTO(b));
        }

        if (book.isPresent()) {
            log.warn("Book with id={} is not borrowed", bookId);
            throw new BookNotAvailableException(ErrorMapper.BOOK_IS_NOT_BORROWED);
        }
        log.warn("Book with id={} not found", bookId);
        throw new NotFoundException(ErrorMapper.BOOK_NOT_FOUND);
    }

    public Optional<List<BookResponseDTO>> getCurrentBorrowedByCustomer(BookRequestDTO bookRequestDTO) {
        log.info("Request all books borrowed by customerId={}", bookRequestDTO.getCustomerId());
        bookRequestDTO.validateCustomerId();

        UUID customerId = UUID.fromString(bookRequestDTO.getCustomerId());
        validateCustomer(customerId);

        List<Book> books = bookRepository.findAllNotAvailableByCustomerId(customerId);

        if (books.isEmpty()) {
            log.info("The customer has no books borrowed");
        }

        log.info("Customer borrows {} books", books.size());
        return Optional.of(books.stream().map(b -> mapper.toDTO(b)).toList());
    }

    public Optional<List<BookResponseDTO>> getBookHistoryByCustomerId(BookRequestDTO bookRequestDTO) {
        log.info("Request borrowing history by customerId={}", bookRequestDTO.getCustomerId());
        bookRequestDTO.validateCustomerId();
        
        UUID customerId = UUID.fromString(bookRequestDTO.getCustomerId());
        validateCustomer(customerId);
        
        List<Transaction> transactions = transactionRepository.findAllByCustomerId(customerId);
        
        if (transactions.isEmpty()) {
            log.info("The customerId={} has no books borrowed", customerId);
            return Optional.of(Collections.emptyList());
        }

        List<BookResponseDTO> history = transactions.stream()
                .filter(transaction -> transaction.getType() == Transaction.Type.BORROW)
                .map(transaction -> {
                    BookResponseDTO historyBook = new BookResponseDTO(transaction);
                    historyBook.setBorrowedAt(transaction.getCreatedAt());

                    transactions.stream()
                            .filter(tx -> tx.getBook().getId().equals(transaction.getBook().getId()) && tx.getType() == Transaction.Type.RETURN)
                            .findFirst()
                            .ifPresent(tx -> historyBook.setReturnedAt(tx.getBook(), tx));
                    return historyBook;
                })
                .collect(Collectors.toList());
        log.info("Customer borrowed a total of {} books", history.size());

        return Optional.of(history);
    }

    private void validateCustomer(UUID id) {
        log.info("Find customer by id={}", id);
        boolean existsCustomer = customerRepository.existsById(id);

        if (!existsCustomer) {
            log.warn("Customer with id={} not found", id);
            throw new NotFoundException(ErrorMapper.CUSTOMER_NOT_FOUND);
        }

        log.info("Customer with id={} found", id);
    }
}
