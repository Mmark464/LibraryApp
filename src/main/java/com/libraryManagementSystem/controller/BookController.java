package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.dto.BookRequest;
import com.libraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@EnableMethodSecurity
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookDto> saveBook(@RequestBody BookRequest bookRequest) {
        BookDto response = bookService.saveBook(bookRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<BookDto>> getAllBooks(
            @PathVariable int pageNumber,
            @PathVariable int pageSize) {
        Page<BookDto> response = bookService.getAllBooks(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto response = bookService.getBookById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<BookDto>> bookFilter(@RequestParam(required = false) Long publishingHouseId,
                                                    @RequestParam(required = false) Set<Long> authorIds,
                                                    @RequestParam(required = false) Set<Long> genreIds,
                                                    @RequestParam(required = false) String title,
                                                    @PathVariable int pageNumber,
                                                    @PathVariable int pageSize) {
        Page<BookDto> response = bookService.bookFilter(publishingHouseId, authorIds, genreIds, title, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
        BookDto response = bookService.updateBook(bookDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}