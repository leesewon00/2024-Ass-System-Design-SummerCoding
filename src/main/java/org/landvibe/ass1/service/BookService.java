package org.landvibe.ass1.service;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.converter.BookConverter;
import org.landvibe.ass1.dto.BookRequestDTO;
import org.landvibe.ass1.dto.BookResponseDTO;
import org.landvibe.ass1.domain.Book;
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
    public BookResponseDTO.CreateResultDTO saveBook(BookRequestDTO.CreateDTO createDTO) {

        Book book = bookRepository.insert(BookConverter.toBook(createDTO));
        return BookConverter.toCreateResultDTO(book);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO.FindResultDTO findById(Long id) throws BookException {

        Book book = bookRepository.findById(id);
        return BookConverter.toFindResultDTO(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO.FindResultDTO> findAll() {

        List<Book> bookList = bookRepository.findAll();
        return BookConverter.toFindResultDTOList(bookList);
    }
}
