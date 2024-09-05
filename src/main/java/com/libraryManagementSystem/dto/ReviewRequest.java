package com.libraryManagementSystem.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private BigDecimal rating;
    private String comment;
    private BookDto book;
}
