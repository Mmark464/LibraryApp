package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;

public interface MemberService {
    void borrowBook(Long bookId, Long userId);

    Page<BookDto> getBorrowedBooks(Long userId, int pageNumber, int pageSize);

    void returnBook(Long bookId, Long userId);
}
