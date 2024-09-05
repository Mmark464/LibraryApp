package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.AuthorDto;
import com.libraryManagementSystem.dto.AuthorRequest;
import com.libraryManagementSystem.entity.Author;
import com.libraryManagementSystem.mapper.AuthorMapper;
import com.libraryManagementSystem.repository.AuthorRepository;
import com.libraryManagementSystem.service.impl.AuthorServiceImpl;
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
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorMapper authorMapper;

    private final Long id = 1L;
    private AuthorRequest authorRequest;
    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    void init(){
        authorRequest = new AuthorRequest();
        authorRequest.setFirstName("firstName");
        authorRequest.setLastName("lastName");
        authorRequest.setBio("bio");

        author = new Author();
        author.setId(id);
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        author.setBio(authorRequest.getBio());

        authorDto = new AuthorDto();
        authorDto.setId(id);
        authorDto.setFirstName(authorRequest.getFirstName());
        authorDto.setLastName(authorRequest.getLastName());
        authorDto.setBio(authorRequest.getBio());
    }

    @Test
    public void saveAuthor() {

        when(authorMapper.requestToEntity(authorRequest)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.saveAuthor(authorRequest);

        assertNotNull(result);
        assertEquals(authorDto, result);
    }

    @Test
    public void getAllAuthors() {
        Author author2 = new Author();
        author2.setId(id + 1);
        author2.setFirstName(authorRequest.getFirstName() + "2");
        author2.setLastName(authorRequest.getLastName() + "2");
        author2.setBio(authorRequest.getBio() + "2");

        AuthorDto authorDto2 = new AuthorDto();
        authorDto2.setId(id + 1);
        authorDto2.setFirstName(authorRequest.getFirstName() + "2");
        authorDto2.setLastName(authorRequest.getLastName() + "2");
        authorDto2.setBio(authorRequest.getBio() + "2");

        List<Author> authors = new ArrayList<>();
        authors.add(author);
        authors.add(author2);

        int pageSize = 10;
        int pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Author> authorPage = new PageImpl<>(authors, pageable, authors.size());

        when(authorRepository.findAllByIsEnabledTrue(pageable)).thenReturn(authorPage);
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);
        when(authorMapper.entityToDto(author2)).thenReturn(authorDto2);

        Page<AuthorDto> result = authorService.getAllAuthors(pageNumber, pageSize);

        assertNotNull(result);
        List<AuthorDto> content = result.getContent();
        assertEquals(2, content.size());
        assertEquals(authorDto, content.get(0));
        assertEquals(authorDto2, content.get(1));
    }

    @Test
    public void getAuthorById() {

        when(authorRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(author));
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.getAuthorById(id);

        assertNotNull(result);
        assertEquals(authorDto, result);
    }

    @Test
    public void updateAuthor() {

        Author updatedAuthor = new Author();
        updatedAuthor.setId(id);
        updatedAuthor.setFirstName("New " + authorRequest.getFirstName());
        updatedAuthor.setLastName("New " + authorRequest.getLastName());
        updatedAuthor.setBio("New " + authorRequest.getBio());

        authorDto.setId(id);
        authorDto.setFirstName("New " + authorRequest.getFirstName());
        authorDto.setLastName("New " + authorRequest.getLastName());
        authorDto.setBio("New " + authorRequest.getBio());

        doNothing().when(authorMapper).updateEntityFromDto(authorDto, author);
        when(authorRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(updatedAuthor);
        when(authorMapper.entityToDto(updatedAuthor)).thenReturn(authorDto);

        AuthorDto result = authorService.updateAuthor(authorDto);

        assertNotNull(result);
        assertEquals(authorDto, result);
    }

    @Test
    public void deleteAuthor() {

        when(authorRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(author));
        authorService.deleteAuthor(id);

        assertFalse(author.getIsEnabled());
    }
}
