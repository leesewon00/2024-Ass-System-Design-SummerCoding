package org.landvibe.ass1.repository;

import org.landvibe.ass1.entity.Book;

import java.util.List;

public interface BookRepository {
    Book insert(Book book);
    Book findById(Long id);
    List<Book> findAll();
    Book update(Book book);
}
