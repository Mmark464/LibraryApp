package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.ReviewDto;
import com.libraryManagementSystem.dto.ReviewRequest;
import com.libraryManagementSystem.entity.Review;
import com.libraryManagementSystem.mapper.ReviewMapper;
import com.libraryManagementSystem.repository.ReviewRepository;
import com.libraryManagementSystem.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    private final Long id = 1L;
    private ReviewRequest reviewRequest;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach
    void init() {
        reviewRequest = new ReviewRequest();

        review = new Review();
        review.setId(id);

        reviewDto = new ReviewDto();
        reviewDto.setId(id);
    }

    @Test
    public void saveReview() {
        when(reviewMapper.requestToEntity(reviewRequest)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.entityToDto(review)).thenReturn(reviewDto);

        ReviewDto result = reviewService.saveReview(reviewRequest);

        assertEquals(reviewDto, result);
    }

    @Test
    public void getAllReviews() {
        Review review2 = new Review();

        ReviewDto reviewDto2 = new ReviewDto();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        reviews.add(review2);

        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Review> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());

        when(reviewRepository.findAllByIsEnabledTrue(pageable)).thenReturn(reviewPage);
        when(reviewMapper.entityToDto(review)).thenReturn(reviewDto);
        when(reviewMapper.entityToDto(review2)).thenReturn(reviewDto2);

        Page<ReviewDto> result = reviewService.getAllReviews(pageNumber, pageSize);

        assertEquals(reviewDto, result.getContent().getFirst());
        assertEquals(reviewDto2, result.getContent().getLast());
    }

    @Test
    public void getReviewById() {
        when(reviewRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(review));
        when(reviewMapper.entityToDto(review)).thenReturn(reviewDto);

        ReviewDto result = reviewService.getReviewById(id);

        assertEquals(reviewDto, result);
    }

    @Test
    public void updateReview() {
        Review updatedReview = new Review();
        updatedReview.setId(id);

        doNothing().when(reviewMapper).updateEntityFromDto(reviewDto, review);
        when(reviewRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(review));
        when(reviewRepository.save(updatedReview)).thenReturn(updatedReview);
        when(reviewMapper.entityToDto(updatedReview)).thenReturn(reviewDto);

        ReviewDto result = reviewService.updateReview(reviewDto);

        assertEquals(reviewDto, result);
    }

    @Test
    public void deleteReview() {
        when(reviewRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(review));
        reviewService.deleteReview(id);

        assertFalse(review.getIsEnabled());
    }
}
