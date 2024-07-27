package org.landvibe.ass1.service;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.entity.Book;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    //cacheable spring docs
    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html

    private final BookRepository bookRepository;

    // put
    @Transactional
    @CachePut(value = "commonBookCache", key = "'book::' + #result.id") // result.id로 함수 실행 후 값을 받아올 수 있다.
    public Book saveBook(Book book) {

        return bookRepository.insert(book);
    }

    @Transactional
    @Cacheable(value = "commonBookCache", key = "'book::' + #book.id") // common::book::1
    public Book updateBook(Book book) {

        return bookRepository.update(book);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "commonBookCache", key = "'book::' + #id") // common::book::1
    public Book findById(Long id) throws BookException {

        return bookRepository.findById(id);
    }

    // list
    @Transactional(readOnly = true)
    @Cacheable(value = "specialBookCache", key = "'book::findAll'") // special::book::findAll
    public List<Book> findAll() {

        List<Book> bookList = bookRepository.findAll();
        return bookList;
    }

    // evict
    @Transactional(readOnly = true)
    @CacheEvict(value = "commonBookCache", key = "'book::' + #id") // common::book::1
    public Book cacheOutById(Long id) throws BookException {

        return bookRepository.findById(id);
    }
}
