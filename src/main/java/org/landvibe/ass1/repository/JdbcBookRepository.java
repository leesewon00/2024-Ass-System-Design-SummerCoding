package org.landvibe.ass1.repository;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.domain.Book;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.exception.ErrorMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    /**
     * Reference
     * https://www.baeldung.com/spring-jdbc-jdbctemplate
     */

    private final JdbcTemplate jdbcTemplate;

    public class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book(rs.getLong("id"), rs.getString("title"));
            return book;
        }
    }

    @Override
    public Book insert(Book book) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert
                .withTableName("book")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new Book(key.longValue(), book.getTitle());
    }

    @Override
    public Book findById(Long id) {
        String query = "SELECT * FROM book WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(query, new BookRowMapper(), id);
        } catch (RuntimeException e) {
            throw new BookException(ErrorMessage.INVALID_ID);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book";
        List<Book> bookList = jdbcTemplate.query(sql, new BookRowMapper());
        return bookList;
    }
}