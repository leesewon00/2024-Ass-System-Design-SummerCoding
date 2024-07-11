package org.landvibe.ass1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.landvibe.ass1.domain.Book;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.repository.JdbcBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.landvibe.ass1.exception.ErrorMessage.INVALID_ID;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcBookRepositoryTest {

    @Autowired
    JdbcBookRepository jdbcBookRepository;

    Book createBook() {
        return Book.builder()
                .title("title")
                .build();
    }

    @Test
    @DisplayName("insert")
    @Transactional
    void insert() {
        Book book = createBook();

        Book insert = jdbcBookRepository.insert(book);

        assertThat(book.getTitle()).isEqualTo(jdbcBookRepository.findById(insert.getId()).getTitle());
    }

    @Test
    @DisplayName("findById")
    @Transactional
    void findById() {
        Book book = jdbcBookRepository.insert(createBook());

        assertThat(jdbcBookRepository.findById(book.getId())).isEqualTo(book);
    }

    @Test
    @DisplayName("findById_fail_invalid_id")
    @Transactional
    void findById_fail() {

        BookException exception = assertThrows(BookException.class, () -> {
            jdbcBookRepository.findById(null);
        });

        assertThat(exception.getMessage()).isEqualTo(INVALID_ID.message);
    }

    @Test
    @DisplayName("findAll")
    @Transactional
    void findAll() {
        Book first = createBook();
        Book second = createBook();
        jdbcBookRepository.insert(first);
        jdbcBookRepository.insert(second);

        List<Book> bookList = jdbcBookRepository.findAll();

        assertThat(bookList.size()).isEqualTo(2);
    }
}

