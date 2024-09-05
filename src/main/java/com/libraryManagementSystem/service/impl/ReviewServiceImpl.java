package com.libraryManagementSystem.service.impl;

import com.libraryManagementSystem.repository.*;
import org.springframework.cache.annotation.*;
import com.libraryManagementSystem.service.*;
import com.libraryManagementSystem.mapper.*;
import com.libraryManagementSystem.entity.*;
import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto saveReview(ReviewRequest reviewRequest) {
        return reviewMapper.entityToDto(
                reviewRepository.save(
                        reviewMapper.requestToEntity(
                                reviewRequest)));
    }

    @Override
    @Cacheable(value = "reviews", key = "'allReviews'")
    public Page<ReviewDto> getAllReviews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reviewRepository.findAllByIsEnabledTrue(pageable)
                .map(reviewMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "reviews", key = "'reviewById_' + #id")
    public ReviewDto getReviewById(Long id) {
        return reviewMapper.entityToDto(findById(id));
    }

    @Override
    @CachePut(value = "reviews", key = "'reviewById_' + #reviewDto.id")
    public ReviewDto updateReview(ReviewDto reviewDto) {
        reviewMapper.updateEntityFromDto(reviewDto, findById(reviewDto.getId()));
        return reviewMapper.entityToDto(
                reviewRepository.save(
                        findById(
                                reviewDto.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "reviews", key = "'allReviews'", allEntries = true),
            @CacheEvict(value = "reviews", key = "'reviewById_' + #id")
    })
    public void deleteReview(Long id) {
        findById(id).setIsEnabled(false);
        reviewRepository.save(findById(id));
    }

    private Review findById(Long id) {
        return reviewRepository.findByIdAndIsEnabledTrue(id).orElseThrow(() ->
                new NumberFormatException("Review not found with ID: " + id));
    }
}
