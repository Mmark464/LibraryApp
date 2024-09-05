package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.PublishingHouseDto;
import com.libraryManagementSystem.dto.PublishingHouseRequest;
import com.libraryManagementSystem.service.PublishingHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity
@RequiredArgsConstructor
@RequestMapping("/api/v1/publishing-house")
public class PublishingHouseController {

    private final PublishingHouseService publishingHouseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<PublishingHouseDto> savePublishingHouse(
            @RequestBody PublishingHouseRequest publishingHouseRequest) {
        PublishingHouseDto response = publishingHouseService
                .savePublishingHouse(publishingHouseRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<PublishingHouseDto>> getAllPublishingHouses(
            @PathVariable int pageNumber,
            @PathVariable int pageSize) {
        Page<PublishingHouseDto> response = publishingHouseService.getAllPublishingHouses(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublishingHouseDto> getPublishingHouseById(@PathVariable long id) {
        PublishingHouseDto response = publishingHouseService.getPublishingHouseById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<PublishingHouseDto> updatePublishingHouse(
            @RequestBody PublishingHouseDto publishingHouseDto) {
        PublishingHouseDto response = publishingHouseService.updatePublishingHouse(publishingHouseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public void deletePublishingHouse(@PathVariable long id) {
        publishingHouseService.deletePublishingHouse(id);
    }
}
