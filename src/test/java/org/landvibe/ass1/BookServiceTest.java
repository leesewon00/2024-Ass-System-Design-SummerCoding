package org.landvibe.ass1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.landvibe.ass1.converter.BookConverter;
import org.landvibe.ass1.entity.Book;
import org.landvibe.ass1.dto.BookRequestDTO;
import org.landvibe.ass1.dto.BookResponseDTO;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.repository.BookRepository;
import org.landvibe.ass1.service.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.landvibe.ass1.exception.ErrorMessage.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    BookService bookService;
    @Mock
    BookRepository bookRepository;

    BookRequestDTO.CreateDTO createCreateDTO() {
        return BookRequestDTO.CreateDTO.builder()
                .title("title")
                .build();
    }

    Book createBook() {
        return Book.builder()
                .id(1L)
                .title("title")
                .build();
    }

//    @Test
//    @DisplayName("saveBook")
//    void saveBook() {
//
//        Book book = createBook();
//        BookRequestDTO.CreateDTO createDTO = createCreateDTO();
//        when(bookRepository.insert(BookConverter.toBook(createDTO))).thenReturn(book);
//
//        BookResponseDTO.CreateResultDTO createResultDTO = bookService.saveBook(createDTO);
//
//        assertThat(createResultDTO).isEqualTo(BookConverter.toCreateResultDTO(book));
//        verify(bookRepository, times(1)).insert(BookConverter.toBook(createDTO));
//    }

//    @Test
//    @DisplayName("findById")
//    void findById() {
//
//        Book book = createBook();
//        when(bookRepository.findById(book.getId())).thenReturn(book);
//
//        BookResponseDTO.FindResultDTO findResultDTO = bookService.findById(book.getId());
//
//        assertThat(findResultDTO).isEqualTo(BookConverter.toFindResultDTO(book));
//        verify(bookRepository, times(1)).findById(book.getId());
//
//    }

    @Test
    @DisplayName("findById_fail_invalid_id")
    void findById_fail() {

        Long id = 1L;
        when(bookRepository.findById(id)).thenThrow(new BookException(INVALID_ID));

        BookException exception = assertThrows(BookException.class, () -> {
            bookService.findById(id);
        });

        assertThat(exception.getMessage()).isEqualTo(INVALID_ID.message);
        verify(bookRepository, times(1)).findById(id);
    }

//    @Test
//    @DisplayName("findAll")
//    void findAll() {
//
//        List<Book> bookList = new ArrayList<>();
//        when(bookRepository.findAll()).thenReturn(bookList);
//
//        List<BookResponseDTO.FindResultDTO> findResultDTOList = bookService.findAll();
//
//        assertThat(findResultDTOList).isEqualTo(BookConverter.toFindResultDTOList(bookList));
//        verify(bookRepository, times(1)).findAll();
//    }

}
