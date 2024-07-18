package org.landvibe.ass1.controller;

import lombok.extern.slf4j.Slf4j;
import org.landvibe.ass1.dto.BookRequestDTO;
import org.landvibe.ass1.dto.BookResponseDTO;
import jakarta.validation.Valid;
import org.landvibe.ass1.exception.BookException;
import org.landvibe.ass1.exception.ErrorMessage;
import org.landvibe.ass1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class BookController {

    private final BookService bookService;
    private AtomicInteger cnt;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
        this.cnt = new AtomicInteger(0);
    }

    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@Valid @RequestBody BookRequestDTO.CreateDTO createDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BookException(ErrorMessage.INVALID_TITLE);
        }
        BookResponseDTO.CreateResultDTO createResultDTO = bookService.saveBook(createDTO);
        log.info(cnt.addAndGet(1) + "번째 요청입니다.");
        return ResponseEntity
                .ok()
                .body(createResultDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        BookResponseDTO.FindResultDTO findResultDTO = bookService.findById(id);
        log.info(cnt.addAndGet(1) + "번째 요청입니다.");
        return ResponseEntity
                .ok()
                .body(findResultDTO);
    }

    @GetMapping("/book")
    public ResponseEntity<?> findAll() {

        List<BookResponseDTO.FindResultDTO> findResultDTOList = bookService.findAll();
        log.info(cnt.addAndGet(1) + "번째 요청입니다.");
        return ResponseEntity
                .ok()
                .body(findResultDTOList);
    }
}
