package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.Author;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByIdAndIsEnabledTrue(Long id);
    Page<Author> findAllByIsEnabledTrue(Pageable pageable);
}
