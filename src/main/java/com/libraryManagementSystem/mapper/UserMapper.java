package com.libraryManagementSystem.mapper;

import com.libraryManagementSystem.dto.*;
import com.libraryManagementSystem.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "stringToCharArray")
    User requestToEntity(UserRequest userRequest);

    UserDto entityToDto(User entity);

    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);

        @Named("stringToCharArray")
    default char[] stringToCharArray(String password) {
        if (password == null) {
            return null;
        }
        return password.toCharArray();
    }
}
