package org.landvibe.ass1.converter;


import org.landvibe.ass1.entity.Book;
import org.landvibe.ass1.dto.BookRequestDTO;
import org.landvibe.ass1.dto.BookResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class BookConverter {

    public static Book toBook(BookRequestDTO.CreateDTO createDTO) {

        return Book.builder()
                .title(createDTO.getTitle())
                .build();
    }

    public static Book toBook(BookRequestDTO.UpdateDTO updateDTO) {

        return Book.builder()
                .id(updateDTO.getId())
                .title(updateDTO.getTitle())
                .build();
    }

    public static BookResponseDTO.CreateResultDTO toCreateResultDTO(Book book) {

        return BookResponseDTO.CreateResultDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build();
    }

    public static BookResponseDTO.UpdateResultDTO toUpdateResultDTO(Book book) {

        return BookResponseDTO.UpdateResultDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build();
    }

    public static BookResponseDTO.FindResultDTO toFindResultDTO(Book book) {

        return BookResponseDTO.FindResultDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .build();
    }

    public static List<BookResponseDTO.FindResultDTO> toFindResultDTOList(List<Book> bookList) {

        return bookList.stream()
                .map(book -> toFindResultDTO(book))
                .collect(Collectors.toList());
    }
}
