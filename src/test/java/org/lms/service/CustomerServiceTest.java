package org.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.dto.response.CustomerResponseDTO;
import org.lms.mapper.CustomerMapper;
import org.lms.model.Customer;
import org.lms.repository.CustomerRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("John");
        customer.setLastName("Doe");

        customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setFirstName("John");
        customerResponseDTO.setLastName("Doe");
    }

    @Test
    public void testGetAllCustomers_EmptyList() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        Optional<List<CustomerResponseDTO>> result = customerService.getAllCustomers();

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = Collections.singletonList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDTO(customer)).thenReturn(customerResponseDTO);

        Optional<List<CustomerResponseDTO>> result = customerService.getAllCustomers();

        assertEquals(1, result.get().size());
        assertEquals(customerResponseDTO, result.get().get(0));
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toDTO(customer);
    }

    @Test
    public void testGetAllNotAvailableByCustomerId() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        List<BookResponseDTO> bookResponseDTOList = Arrays.asList(new BookResponseDTO(), new BookResponseDTO());

        when(bookService.getCurrentBorrowedByCustomer(bookRequestDTO)).thenReturn(Optional.of(bookResponseDTOList));

        Optional<List<BookResponseDTO>> result = customerService.getAllNotAvailableByCustomerId(bookRequestDTO);

        assertEquals(2, result.get().size());
        verify(bookService, times(1)).getCurrentBorrowedByCustomer(bookRequestDTO);
    }

    @Test
    public void testGetBookHistoryByCustomerId() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        List<BookResponseDTO> bookResponseDTOList = Arrays.asList(new BookResponseDTO(), new BookResponseDTO());

        when(bookService.getBookHistoryByCustomerId(bookRequestDTO)).thenReturn(Optional.of(bookResponseDTOList));

        Optional<List<BookResponseDTO>> result = customerService.getBookHistoryByCustomerId(bookRequestDTO);

        assertEquals(2, result.get().size());
        verify(bookService, times(1)).getBookHistoryByCustomerId(bookRequestDTO);
    }
}