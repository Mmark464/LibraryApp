package com.libraryManagementSystem.dto;

import lombok.Data;

@Data
public class BorrowBookRequest {
    private Long bookId;
    private Long userId;
}
