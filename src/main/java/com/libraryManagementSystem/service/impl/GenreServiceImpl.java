package com.libraryManagementSystem.service.impl;

import com.libraryManagementSystem.repository.*;
import org.springframework.cache.annotation.*;
import com.libraryManagementSystem.service.*;
import com.libraryManagementSystem.mapper.*;
import com.libraryManagementSystem.entity.*;
import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDto saveGenre(GenreRequest genreRequest) {
        return genreMapper.entityToDto(
                genreRepository.save(
                        genreMapper.requestToEntity(
                                genreRequest
                        )));
    }

    @Override
    @Cacheable(value = "genres", key = "'allGenres'")
    public Page<GenreDto> getAllGenres(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return genreRepository.findAllByIsEnabledTrue(pageable)
                .map(genreMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "genres", key = "'genreById_' + #id")
    public GenreDto getGenreById(Long id) {
        return genreMapper.entityToDto(
                findById(id)
        );
    }

    @Override
    @CachePut(value = "genres", key = "'genreById_' + #genreDto.id")
    public GenreDto updateGenre(GenreDto genreDto) {

        genreMapper.updateEntityFromDto(genreDto, findById(genreDto.getId()));

        return genreMapper.entityToDto(
                genreRepository.save(
                        findById(
                                genreDto.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "genres", key = "'allGenres'", allEntries = true),
            @CacheEvict(value = "genres", key = "'genreById_' + #id")
    })
    public void deleteGenre(Long id) {
        findById(id).setIsEnabled(false);
        genreRepository.save(
                findById(id)
        );
    }

    private Genre findById(Long id) {
        return genreRepository.findByIdAndIsEnabledTrue(id).orElseThrow(() ->
                new NumberFormatException("Genre not found with ID: " + id));
    }
}