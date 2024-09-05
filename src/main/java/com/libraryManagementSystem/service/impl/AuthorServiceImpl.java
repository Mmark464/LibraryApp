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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorDto saveAuthor(AuthorRequest authorRequest) {
        return authorMapper.entityToDto(
                authorRepository.save(
                        authorMapper.requestToEntity(
                                authorRequest
                        )));
    }

    @Override
    @Cacheable(value = "authors", key = "'allAuthors'")
    public Page<AuthorDto> getAllAuthors(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return authorRepository.findAllByIsEnabledTrue(pageable)
                               .map(authorMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "authors", key = "'authorById_' + #id")
    public AuthorDto getAuthorById(Long id){
        return authorMapper.entityToDto(
                findById(id)
        );
    }

    @Override
    @Transactional
    @CachePut(value = "authors", key = "'authorById_' + #authorDto.id")
    public AuthorDto updateAuthor(AuthorDto authorDto) {

        authorMapper.updateEntityFromDto(authorDto, findById(authorDto.getId()));

        return authorMapper.entityToDto(
                authorRepository.save(
                        findById(
                                authorDto.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "authors", key = "'allAuthors'", allEntries = true),
            @CacheEvict(value = "authors", key = "'authorById_' + #id")
    })
    public void deleteAuthor(Long id) {
        findById(id).setIsEnabled(false);
        authorRepository.save(
                findById(id)
        );
    }

    private Author findById(Long id) {
        return authorRepository.findByIdAndIsEnabledTrue(id)
                .orElseThrow(() -> new NumberFormatException("Author not found with id: " + id));
    }
}
