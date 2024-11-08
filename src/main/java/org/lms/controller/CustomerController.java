package org.lms.controller;

import lombok.AllArgsConstructor;
import org.lms.dto.request.BookRequestDTO;
import org.lms.dto.response.BookResponseDTO;
import org.lms.dto.response.CustomerResponseDTO;
import org.lms.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return customerService.getAllCustomers()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }

    @GetMapping("/{id}/borrowed-books")
    public ResponseEntity<List<BookResponseDTO>> getAllNotAvailableByCustomerId(@PathVariable String id) {
        return customerService.getAllNotAvailableByCustomerId(new BookRequestDTO(id))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<BookResponseDTO>> getCustomerHistory(@PathVariable String id) {
        return customerService.getBookHistoryByCustomerId(new BookRequestDTO(id))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.of(Optional.empty()));
    }
}
