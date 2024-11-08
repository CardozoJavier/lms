package org.lms.repository;

import lombok.AllArgsConstructor;
import org.lms.model.Book;
import org.lms.model.Transaction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS");

    public void insertTransaction(Transaction.Type type, UUID customerId, UUID bookId) {
        String sql = "INSERT INTO transaction (type, customer_id, book_id) VALUES (:type, :customerId, :bookId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("type", type.toString())
                .addValue("createdAt", LocalDateTime.now())
                .addValue("updatedAt", LocalDateTime.now())
                .addValue("customerId", customerId)
                .addValue("bookId", bookId);
        jdbcTemplate.update(sql, params);
    }

    public List<Transaction> findAllByCustomerId(UUID customerId) {
        String sql = "SELECT tx.id as transaction_id, tx.created_at as transaction_created_at, tx.type as transaction_type, " +
                "book.id as book_id, book.title, book.category, book.genre, book.author, book.language, book.returned_at, book.available" +
                "    FROM transaction tx" +
                "    INNER JOIN book ON book.id = tx.book_id" +
                "    WHERE tx.customer_id = :customer_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("customer_id", customerId);

        List<Transaction> result = jdbcTemplate.query(sql, params, transactionMapper);
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    private final RowMapper<Transaction> transactionMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getObject("book_id", UUID.class));
        book.setTitle(rs.getString("title"));
        book.setCategory(rs.getString("category"));
        book.setGenre(rs.getString("genre"));
        book.setAuthor(rs.getString("author"));
        book.setLanguage(rs.getString("language"));
        book.setAvailable(rs.getBoolean("available"));

        return new Transaction(
                UUID.fromString(rs.getString("transaction_id")),
                Transaction.Type.valueOf(rs.getString("transaction_type")),
                rs.getObject("transaction_created_at", LocalDateTime.class),
                null,
                null,
                UUID.fromString(rs.getString("book_id")),
                book);
    };
}
