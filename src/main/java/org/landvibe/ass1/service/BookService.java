package org.landvibe.ass1.service;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.cache.annotation.CacheOutLandVibe;
import org.landvibe.ass1.cache.annotation.CachingLandVibe;
import org.landvibe.ass1.entity.Book;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    @CachingLandVibe(key = "book:{0}", cacheManager = "bookCacheManager", method = "POST")
    public Book saveBook(Book book) {

        return bookRepository.insert(book);
    }

    @Transactional
    @CachingLandVibe(key = "book:{0}", cacheManager = "bookCacheManager", method = "PATCH")
    public Book updateBook(Book book) {

        return bookRepository.update(book);
    }

    @Transactional(readOnly = true)
    @CachingLandVibe(key = "book:{0}", cacheManager = "bookCacheManager", method = "GET")
    public Book findById(Long id) throws BookException {

        return bookRepository.findById(id);
    }

    // list
    @Transactional(readOnly = true)
    @CachingLandVibe(key = "book", cacheManager = "bookCacheManager", method = "GET")
    public List<Book> findAll() {

        List<Book> bookList = bookRepository.findAll();
        return bookList;
    }

    // evict
    @Transactional(readOnly = true)
    @CacheOutLandVibe(key = "book:{0}", cacheManager = "bookCacheManager")
    public Book cacheOutById(Long id) throws BookException {

        return bookRepository.findById(id);
    }
}
