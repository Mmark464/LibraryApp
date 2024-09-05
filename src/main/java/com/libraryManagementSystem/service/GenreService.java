package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;

public interface GenreService {
    GenreDto saveGenre(GenreRequest genreRequest);

    Page<GenreDto> getAllGenres(int pageNumber, int pageSize);

    GenreDto getGenreById(Long id);

    GenreDto updateGenre(GenreDto genre);

    void deleteGenre(Long id);
}
