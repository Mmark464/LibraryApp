package com.libraryManagementSystem.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
}
