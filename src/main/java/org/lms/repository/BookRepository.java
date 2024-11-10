package org.lms.repository;

import lombok.AllArgsConstructor;
import org.lms.exception.BadRequestException;
import org.lms.mapper.ErrorMapper;
import org.lms.model.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@AllArgsConstructor
public class BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long countAllByAvailableTrue() {
        String sql = "SELECT COUNT(*) FROM book WHERE available = TRUE";
        try {
            Long count = jdbcTemplate.queryForObject(sql, new HashMap<>(), Long.class);
            return count != null ? count : 0;
        } catch (DataAccessException e) {
            return 0L;
        }
    }

    public List<Book> findAllByAvailableTrue(Long totalRows, Integer limit, Integer offset) {
        int maxPage = (int) Math.ceil((double) totalRows / limit) - 1;

        if (offset > maxPage) {
            throw new BadRequestException(ErrorMapper.INVALID_PAGINATION);
        }

        String sql = "SELECT * FROM book WHERE available = TRUE LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("limit", limit)
            .addValue("offset", limit * offset);

        return jdbcTemplate.query(sql, params, liteBookMapper);
    }

    public Optional<Book> updateById(UUID id, UUID customerId, boolean available, LocalDateTime borrowedAt, LocalDateTime returnedAt) {
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("book_id", id)
            .addValue("customer_id", customerId)
            .addValue("available", available)
            .addValue("borrowed_at", borrowedAt)
            .addValue("returned_at", returnedAt);

        String sql = "UPDATE book SET available = :available, borrowed_at = :borrowed_at, returned_at = :returned_at, customer_id = :customer_id " +
                "WHERE book.id = :book_id";
        jdbcTemplate.update(sql, params);

        Book book = findById(id).orElse(null);

        return book == null ? Optional.empty() : Optional.of(book);
    }

    public Optional<Book> findById(UUID id) {
        String sql = "SELECT * FROM book WHERE id = :book_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", id);

        try {
            Book result = jdbcTemplate.queryForObject(sql, params, bookMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Book> findAllNotAvailableByCustomerId(UUID customerId) {
        String sql = "SELECT * FROM book WHERE available = FALSE AND customer_id = :customer_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("customer_id", customerId);

        List<Book> result = jdbcTemplate.query(sql, params, bookMapper);
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    private final RowMapper<Book> liteBookMapper = (rs, rowNum) -> new Book(
        UUID.fromString(rs.getString("id")),
        rs.getString("title"),
        rs.getString("category"),
        rs.getString("genre"),
        rs.getString("author"),
        rs.getString("language"),
        null, null, null, null
    );

    private final RowMapper<Book> bookMapper = (rs, rowNum) -> {
        UUID customerId = null;
        String customerIdStr = rs.getString("customer_id");
        if (customerIdStr != null) {
            customerId = UUID.fromString(customerIdStr);
        }
        return new Book(
            UUID.fromString(rs.getString("id")),
            rs.getString("title"),
            rs.getString("category"),
            rs.getString("genre"),
            rs.getString("author"),
            rs.getString("language"),
            rs.getObject("borrowed_at", LocalDateTime.class),
            rs.getObject("returned_at", LocalDateTime.class),
            rs.getBoolean("available"),
            customerId);
    };
}
