package com.libraryManagementSystem.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z\0-9]{5,20}$",
            message = "Username must be between 5 and 20 characters and contain only letters and numbers")
    private Set<RoleDto> roles;
}
