package com.libraryManagementSystem.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishingHouseDto implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
}
