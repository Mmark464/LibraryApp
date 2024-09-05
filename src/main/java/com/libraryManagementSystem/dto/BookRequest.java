package com.libraryManagementSystem.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private BigDecimal price;
    private ZonedDateTime publicationDate;
    private PublishingHouseDto publishingHouse;
    private Set<AuthorDto> authors;
    private Set<GenreDto> genres;
}
