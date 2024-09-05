package com.libraryManagementSystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto implements Serializable {
    private Long id;
    private BigDecimal rating;
    private String comment;
    private ZonedDateTime lastUpdateDate;
    private BookDto book;
}
