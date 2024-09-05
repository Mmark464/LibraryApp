package com.libraryManagementSystem.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^[a-zA-Z\0-9]{5,20}$",
            message = "Username must be between 5 and 20 characters and contain only letters and numbers")
    private String username;

    @Pattern(regexp = "^.{8,20}$", message = "Password length must be between 8-20 characters.")
    private String password;

    private Set<RoleDto> roles;
}
