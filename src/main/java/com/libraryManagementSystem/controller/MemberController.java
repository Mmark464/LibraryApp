package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.dto.BorrowBookRequest;
import com.libraryManagementSystem.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<String> borrowBook(@RequestBody BorrowBookRequest borrowBookRequest) {
        memberService.borrowBook(borrowBookRequest.getBookId(), borrowBookRequest.getUserId());
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @GetMapping("/{id}/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<BookDto>> getBorrowedBooks(@PathVariable Long id,
                                                          @PathVariable int pageNumber,
                                                          @PathVariable int pageSize) {
        Page<BookDto> borrowedBooks = memberService.getBorrowedBooks(id, pageNumber, pageSize);
        return ResponseEntity.ok(borrowedBooks);
    }

    @DeleteMapping
    public ResponseEntity<String> returnBook(@RequestBody BorrowBookRequest borrowBookRequest) {
        memberService.returnBook(borrowBookRequest.getBookId(), borrowBookRequest.getUserId());
        return ResponseEntity.ok("Book returned successfully");
    }
}
