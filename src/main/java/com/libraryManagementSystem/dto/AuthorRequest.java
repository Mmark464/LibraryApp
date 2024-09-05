package com.libraryManagementSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequest {
    private String firstName;
    private String lastName;
    private String bio;
}
