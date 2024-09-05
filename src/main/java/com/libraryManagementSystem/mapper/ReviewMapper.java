package com.libraryManagementSystem.mapper;

import com.libraryManagementSystem.dto.*;
import com.libraryManagementSystem.entity.Review;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    Review requestToEntity(ReviewRequest reviewRequest);

    @Mapping(target = "lastUpdateDate", source = "updatedAt")
    @Mapping(target = "book.authors", ignore = true)
    @Mapping(target = "book.genres", ignore = true)
    ReviewDto entityToDto(Review entity);

    void updateEntityFromDto(ReviewDto dto, @MappingTarget Review entity);
}
