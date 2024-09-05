package com.libraryManagementSystem.specification;

import com.libraryManagementSystem.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class BookSpecification {

    public static Specification<Book> findBookByCriteria(Long publishingHouseId,
                                                         Set<Long> authorIds,
                                                         Set<Long> genreIds,
                                                         String title) {
        return (root, _, cb) -> {
            Predicate predicate = cb.conjunction();
            if (publishingHouseId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("publishingHouse").get("id"), publishingHouseId));
            }
            if (authorIds != null && !authorIds.isEmpty()) {
                Join<Book, Author> authors = root.join("authors");
                predicate = cb.and(predicate,
                        authors.get("id").in(authorIds));
            }
            if (genreIds != null && !genreIds.isEmpty()) {
                Join<Book, Genre> genres = root.join("genres");
                predicate = cb.and(predicate,
                        genres.get("id").in(genreIds));
            }
            if (title != null && !title.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.get("title"), "%" + title + "%"));
            }
            return predicate;
        };
    }

}
