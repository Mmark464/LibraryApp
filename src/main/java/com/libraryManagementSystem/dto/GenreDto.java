package com.libraryManagementSystem.dto;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto implements Serializable {
    private Long id;
    private String name;
    private String description;
}
