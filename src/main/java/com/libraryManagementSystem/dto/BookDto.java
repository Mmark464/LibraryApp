package com.libraryManagementSystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto implements Serializable {
    private Long id;
    private String title;
    private BigDecimal price;
    private ZonedDateTime publicationDate;
    private BigDecimal averageRating;
    private PublishingHouseDto publishingHouse;
    private Set<AuthorDto> authors;
    private Set<GenreDto> genres;
}
