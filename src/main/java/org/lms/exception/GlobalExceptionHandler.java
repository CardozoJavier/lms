package org.lms.exception;

import lombok.extern.slf4j.Slf4j;
import org.lms.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex) {
        log.error("Not found exception: ", ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        log.error("Bad request exception: ", ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ErrorResponseDTO> handleBookNotAvailableException(BookNotAvailableException ex) {
        log.error("Book not available exception: ", ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
