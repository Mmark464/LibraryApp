package com.libraryManagementSystem.service;


import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;

public interface AuthorService {
    AuthorDto saveAuthor(AuthorRequest authorRequest);

    Page<AuthorDto> getAllAuthors(int pageNumber, int pageSize);

    AuthorDto getAuthorById(Long id);

    AuthorDto updateAuthor(AuthorDto author);

    void deleteAuthor(Long id);
}
