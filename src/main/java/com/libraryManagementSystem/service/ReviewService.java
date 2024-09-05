package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;


public interface ReviewService {
    ReviewDto saveReview(ReviewRequest reviewRequest);

    Page<ReviewDto> getAllReviews(int pageNumber, int pageSize);

    ReviewDto getReviewById(Long id);

    ReviewDto updateReview(ReviewDto review);

    void deleteReview(Long id);
}
