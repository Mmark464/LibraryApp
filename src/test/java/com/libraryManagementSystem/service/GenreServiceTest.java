package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.GenreDto;
import com.libraryManagementSystem.dto.GenreRequest;
import com.libraryManagementSystem.entity.Genre;
import com.libraryManagementSystem.mapper.GenreMapper;
import com.libraryManagementSystem.repository.GenreRepository;
import com.libraryManagementSystem.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Mock
    private GenreMapper genreMapper;

    private final Long id = 1L;
    private GenreRequest genreRequest;
    private Genre genre;
    private GenreDto genreDto;

    @BeforeEach
    void init() {
        genreRequest = new GenreRequest();

        genre = new Genre();
        genre.setId(id);

        genreDto = new GenreDto();
        genreDto.setId(id);
    }

    @Test
    public void saveGenre() {
        when(genreMapper.requestToEntity(genreRequest)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.entityToDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.saveGenre(genreRequest);

        assertEquals(genreDto, result);
    }

    @Test
    public void getAllGenres() {
        Genre genre2 = new Genre();

        GenreDto genreDto2 = new GenreDto();

        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        genres.add(genre2);

        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Genre> genrePage = new PageImpl<>(genres, pageable, genres.size());

        when(genreRepository.findAllByIsEnabledTrue(pageable)).thenReturn(genrePage);
        when(genreMapper.entityToDto(genre)).thenReturn(genreDto);
        when(genreMapper.entityToDto(genre2)).thenReturn(genreDto2);

        Page<GenreDto> result = genreService.getAllGenres(pageNumber, pageSize);

        assertEquals(genreDto, result.getContent().getFirst());
        assertEquals(genreDto2, result.getContent().getLast());
    }

    @Test
    public void getGenreById() {
        when(genreRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(genre));
        when(genreMapper.entityToDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.getGenreById(id);

        assertEquals(genreDto, result);
    }

    @Test
    public void updateGenre() {

        Genre updateGenre = new Genre();
        updateGenre.setId(id);

        doNothing().when(genreMapper).updateEntityFromDto(genreDto, genre);
        when(genreRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(updateGenre);
        when(genreMapper.entityToDto(updateGenre)).thenReturn(genreDto);

        GenreDto result = genreService.updateGenre(genreDto);

        assertEquals(genreDto, result);
    }

    @Test
    public void deleteGenre() {
        when(genreRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(genre));
        genreService.deleteGenre(id);

        assertFalse(genre.getIsEnabled());
    }
}