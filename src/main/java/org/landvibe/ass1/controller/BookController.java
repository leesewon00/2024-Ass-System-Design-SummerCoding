package org.landvibe.ass1.controller;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.converter.BookConverter;
import org.landvibe.ass1.dto.BookRequestDTO;
import org.landvibe.ass1.dto.BookResponseDTO;
import jakarta.validation.Valid;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.exception.ErrorMessage;
import org.landvibe.ass1.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@Valid @RequestBody BookRequestDTO.CreateDTO createDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BookException(ErrorMessage.INVALID_TITLE);
        }
        BookResponseDTO.CreateResultDTO createResultDTO = BookConverter.toCreateResultDTO(
                bookService.saveBook(BookConverter.toBook(createDTO)));
        return ResponseEntity
                .ok()
                .body(createResultDTO);
    }

    @PatchMapping("/book")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookRequestDTO.UpdateDTO updateDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BookException(ErrorMessage.INVALID_TITLE);
        }
        BookResponseDTO.UpdateResultDTO updateResultDTO = BookConverter.toUpdateResultDTO(
                bookService.updateBook(BookConverter.toBook(updateDTO)));
        return ResponseEntity
                .ok()
                .body(updateResultDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        BookResponseDTO.FindResultDTO findResultDTO = BookConverter.toFindResultDTO(bookService.findById(id));
        return ResponseEntity
                .ok()
                .body(findResultDTO);
    }

    // list
    @GetMapping("/book")
    public ResponseEntity<?> findAll() {

        List<BookResponseDTO.FindResultDTO> findResultDTOList = BookConverter.toFindResultDTOList(bookService.findAll());
        return ResponseEntity
                .ok()
                .body(findResultDTOList);
    }

    // evict
    @GetMapping("/book/out/{id}")
    public ResponseEntity<?> cacheOutById(@PathVariable("id") Long id) {

        BookResponseDTO.FindResultDTO findResultDTO = BookConverter.toFindResultDTO(bookService.cacheOutById(id));
        return ResponseEntity
                .ok()
                .body(findResultDTO);
    }
}
