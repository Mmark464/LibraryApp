package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.PublishingHouse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Long> {
    Optional<PublishingHouse> findByIdAndIsEnabledTrue(Long id);

    Page<PublishingHouse> findAllByIsEnabledTrue(Pageable pageable);
}
