package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.GenreDto;
import com.libraryManagementSystem.dto.GenreRequest;
import com.libraryManagementSystem.service.GenreService;
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
@RequestMapping("/api/v1/genre")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<GenreDto> saveGenre(@RequestBody GenreRequest genreRequest) {
        GenreDto response = genreService.saveGenre(genreRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<GenreDto>> getAllGenres(
            @PathVariable int pageNumber,
            @PathVariable int pageSize) {
        Page<GenreDto> response = genreService.getAllGenres(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable long id) {
        GenreDto response = genreService.getGenreById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<GenreDto> updateGenre(@RequestBody GenreDto genreDto) {
        GenreDto response = genreService.updateGenre(genreDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public void deleteGenre(@PathVariable long id) {
        genreService.deleteGenre(id);
    }
}