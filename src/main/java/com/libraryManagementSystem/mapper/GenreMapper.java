package com.libraryManagementSystem.mapper;

import com.libraryManagementSystem.dto.*;
import com.libraryManagementSystem.entity.Genre;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    Genre requestToEntity(GenreRequest genreRequest);

    GenreDto entityToDto(Genre entity);

    void updateEntityFromDto(GenreDto dto, @MappingTarget Genre entity);
}
