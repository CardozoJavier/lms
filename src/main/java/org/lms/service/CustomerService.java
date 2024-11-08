package org.lms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.dto.response.CustomerResponseDTO;
import org.lms.exception.NotFoundException;
import org.lms.mapper.CustomerMapper;
import org.lms.model.Customer;
import org.lms.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
    private BookService bookService;

    public Optional<List<CustomerResponseDTO>> getAllCustomers() {
        log.info("Request all customers");
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            log.warn("Customers not found");
            return Optional.empty();
        }

        return Optional.of(customers.stream().map(c -> customerMapper.toDTO(c)).toList());
    }

    public Optional<List<BookResponseDTO>> getAllNotAvailableByCustomerId(BookRequestDTO bookRequestDTO) {
        return bookService.getCurrentBorrowedByCustomer(bookRequestDTO);
    }

    public Optional<List<BookResponseDTO>> getBookHistoryByCustomerId(BookRequestDTO bookRequestDTO) {
        return bookService.getBookHistoryByCustomerId(bookRequestDTO);
    }
}
