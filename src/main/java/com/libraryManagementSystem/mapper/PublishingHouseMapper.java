package com.libraryManagementSystem.mapper;

import com.libraryManagementSystem.dto.*;
import com.libraryManagementSystem.entity.PublishingHouse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PublishingHouseMapper {

    @Mapping(target = "id", ignore = true)
    PublishingHouse requestToEntity(PublishingHouseRequest publishingHouseRequest);

    PublishingHouseDto entityToDto(PublishingHouse entity);

    void updateEntityFromDto(PublishingHouseDto dto, @MappingTarget PublishingHouse entity);
}
