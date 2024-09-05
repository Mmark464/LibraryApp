package com.libraryManagementSystem.repository;

import com.libraryManagementSystem.entity.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user-roles-graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUsernameAndIsEnabledTrue(String username);

    @EntityGraph(value = "user-roles-graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByIdAndIsEnabledTrue(Long id);

    @EntityGraph(value = "user-roles-graph", type = EntityGraph.EntityGraphType.LOAD)
    Page<User> findAllByIsEnabledTrue(Pageable pageable);
}
