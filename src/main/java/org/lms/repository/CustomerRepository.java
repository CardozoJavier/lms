package org.lms.repository;

import lombok.AllArgsConstructor;
import org.lms.model.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CustomerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM customer WHERE id = :id";
        Integer count = jdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), Integer.class);
        return count != null && count > 0;
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";

        List<Customer> customers = jdbcTemplate.query(sql, customerMapper);
        return customers.isEmpty() ? Collections.emptyList() : customers;
    }

    private final RowMapper<Customer> customerMapper = (rs, rowNum) ->
            new Customer(
                    (UUID) rs.getObject("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getObject("created_at", LocalDateTime.class),
                    rs.getObject("updated_at", LocalDateTime.class)
            );
}
