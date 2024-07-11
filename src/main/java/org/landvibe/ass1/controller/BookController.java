package org.landvibe.ass1.controller;

import lombok.RequiredArgsConstructor;
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
        BookResponseDTO.CreateResultDTO createResultDTO = bookService.saveBook(createDTO);
        return ResponseEntity
                .ok()
                .body(createResultDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        BookResponseDTO.FindResultDTO findResultDTO = bookService.findById(id);
        return ResponseEntity
                .ok()
                .body(findResultDTO);
    }

    @GetMapping("/book")
    public ResponseEntity<?> findAll() {

        List<BookResponseDTO.FindResultDTO> findResultDTOList = bookService.findAll();
        return ResponseEntity
                .ok()
                .body(findResultDTOList);
    }
}
