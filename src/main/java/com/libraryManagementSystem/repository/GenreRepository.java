package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.Genre;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByIdAndIsEnabledTrue(Long id);

    Page<Genre> findAllByIsEnabledTrue(Pageable pageable);
}
