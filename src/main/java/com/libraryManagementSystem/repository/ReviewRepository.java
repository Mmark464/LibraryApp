package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.Review;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndIsEnabledTrue(Long id);

    Page<Review> findAllByIsEnabledTrue(Pageable pageable);
}
