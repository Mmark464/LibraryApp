package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;

import java.util.Set;

public interface BookService {
    BookDto saveBook(BookRequest bookRequest);

    Page<BookDto> getAllBooks(int pageNumber, int pageSize);

    BookDto getBookById(Long id);

    Page<BookDto> bookFilter(Long publishingHouseId,
                             Set<Long> authorIds,
                             Set<Long> genreIds,
                             String title,
                             int pageNumber, int pageSize);

    BookDto updateBook(BookDto book);

    void deleteBook(Long id);

    void updateAverageRating(Long id);
}
