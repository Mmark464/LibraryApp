package com.libraryManagementSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishingHouseRequest {
    private String name;
    private String address;
    private String contactNumber;
}
