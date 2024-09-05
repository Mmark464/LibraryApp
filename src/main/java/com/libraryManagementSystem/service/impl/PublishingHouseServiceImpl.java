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
public class PublishingHouseServiceImpl implements PublishingHouseService {

    private final PublishingHouseRepository publishingHouseRepository;
    private final PublishingHouseMapper publishingHouseMapper;

    @Override
    public PublishingHouseDto savePublishingHouse(PublishingHouseRequest publishingHouseRequest) {
        return publishingHouseMapper.entityToDto(
                publishingHouseRepository.save(
                        publishingHouseMapper.requestToEntity(
                                publishingHouseRequest
                        )));
    }

    @Override
    @Cacheable(value = "publishingHouses", key = "'allPublishingHouses'")
    public Page<PublishingHouseDto> getAllPublishingHouses(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return publishingHouseRepository.findAllByIsEnabledTrue(pageable)
                                        .map(publishingHouseMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "publishingHouses", key = "'publishingHouseById_' + #id")
    public PublishingHouseDto getPublishingHouseById(Long id) {
        return publishingHouseMapper.entityToDto(findById(id));
    }

    @Override
    @CachePut(value = "publishingHouses", key = "'publishingHouseById_' + #publishingHouseDto.id")
    public PublishingHouseDto updatePublishingHouse(PublishingHouseDto publishingHouseDto) {

        publishingHouseMapper.updateEntityFromDto(publishingHouseDto, findById(publishingHouseDto.getId()));
        return publishingHouseMapper.entityToDto(
                publishingHouseRepository.save(
                        findById(
                                publishingHouseDto.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "publishingHouses", key = "'allPublishingHouses'", allEntries = true),
            @CacheEvict(value = "publishingHouses", key = "'publishingHouseById_' + #id")
    })
    public void deletePublishingHouse(Long id) {
        findById(id).setIsEnabled(false);
        publishingHouseRepository.save(findById(id));
    }

    private PublishingHouse findById(Long id) {
        return publishingHouseRepository.findByIdAndIsEnabledTrue(id).orElseThrow(() ->
                new NumberFormatException("PublishingHouse not found with ID: " + id));
    }
}
