package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.ReviewDto;
import com.libraryManagementSystem.dto.ReviewRequest;
import com.libraryManagementSystem.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> saveReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewDto response = reviewService.saveReview(reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ReviewDto>> getAllReviews(
            @PathVariable int pageNumber,
            @PathVariable int pageSize) {
        Page<ReviewDto> response = reviewService.getAllReviews(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto response = reviewService.updateReview(reviewDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
