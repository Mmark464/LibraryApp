package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIdAndIsEnabledTrue(Long id);

    Page<Book> findAllByIsEnabledTrue(Specification<Book> specification, Pageable pageable);

    Page<Book> findAllByIsEnabledTrue(Pageable pageable);
}
