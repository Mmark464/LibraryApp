package com.libraryManagementSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequest {
    private String name;
    private String description;
}
