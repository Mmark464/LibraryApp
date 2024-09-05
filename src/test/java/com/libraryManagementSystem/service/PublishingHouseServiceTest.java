package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.PublishingHouseDto;
import com.libraryManagementSystem.dto.PublishingHouseRequest;
import com.libraryManagementSystem.entity.PublishingHouse;
import com.libraryManagementSystem.mapper.PublishingHouseMapper;
import com.libraryManagementSystem.repository.PublishingHouseRepository;
import com.libraryManagementSystem.service.impl.PublishingHouseServiceImpl;
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
public class PublishingHouseServiceTest {

    @Mock
    private PublishingHouseRepository publishingHouseRepository;

    @InjectMocks
    private PublishingHouseServiceImpl publishingHouseService;

    @Mock
    private PublishingHouseMapper publishingHouseMapper;

    private final Long id = 1L;
    private PublishingHouseRequest publishingHouseRequest;
    private PublishingHouse publishingHouse;
    private PublishingHouseDto publishingHouseDto;

    @BeforeEach
    void init() {
        publishingHouseRequest = new PublishingHouseRequest();

        publishingHouse = new PublishingHouse();
        publishingHouse.setId(id);

        publishingHouseDto = new PublishingHouseDto();
        publishingHouseDto.setId(id);
    }

    @Test
    void savePublishingHouse() {
        when(publishingHouseMapper.requestToEntity(publishingHouseRequest)).thenReturn(publishingHouse);
        when(publishingHouseRepository.save(publishingHouse)).thenReturn(publishingHouse);
        when(publishingHouseMapper.entityToDto(publishingHouse)).thenReturn(publishingHouseDto);

        PublishingHouseDto result = publishingHouseService.savePublishingHouse(publishingHouseRequest);

        assertEquals(publishingHouseDto, result);
    }

    @Test
    void getAllPublishingHouses() {
        PublishingHouse publishingHouse2 = new PublishingHouse();

        PublishingHouseDto publishingHouseDto2 = new PublishingHouseDto();

        List<PublishingHouse> publishingHouses = new ArrayList<>();
        publishingHouses.add(publishingHouse);
        publishingHouses.add(publishingHouse2);

        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        Page<PublishingHouse> publishingHousePage = new PageImpl<>(publishingHouses, pageable, publishingHouses.size());

        when(publishingHouseRepository.findAllByIsEnabledTrue(pageable)).thenReturn(publishingHousePage);
        when(publishingHouseMapper.entityToDto(publishingHouse)).thenReturn(publishingHouseDto);
        when(publishingHouseMapper.entityToDto(publishingHouse2)).thenReturn(publishingHouseDto2);

        Page<PublishingHouseDto> result = publishingHouseService.getAllPublishingHouses(pageSize, pageNumber, pageSort);

        assertEquals(publishingHouseDto, result.getContent().getFirst());
        assertEquals(publishingHouseDto2, result.getContent().getLast());
    }

    @Test
    void getPublishingHouseById() {
        when(publishingHouseRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.ofNullable(publishingHouse));
        when(publishingHouseMapper.entityToDto(publishingHouse)).thenReturn(publishingHouseDto);

        PublishingHouseDto result = publishingHouseService.getPublishingHouseById(id);

        assertEquals(publishingHouseDto, result);
    }

    @Test
    void updatePublishingHouse() {
        PublishingHouse updatePublishingHouse = new PublishingHouse();
        updatePublishingHouse.setId(id);

        doNothing().when(publishingHouseMapper).updateEntityFromDto(publishingHouseDto, publishingHouse);
        when(publishingHouseRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.ofNullable(publishingHouse));
        when(publishingHouseRepository.save(publishingHouse)).thenReturn(publishingHouse);
        when(publishingHouseMapper.entityToDto(publishingHouse)).thenReturn(publishingHouseDto);

        PublishingHouseDto result = publishingHouseService.updatePublishingHouse(publishingHouseDto);

        assertEquals(publishingHouseDto, result);
    }

    @Test
    void deletePublishingHouse() {
        when(publishingHouseRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.ofNullable(publishingHouse));
        publishingHouseService.deletePublishingHouse(id);

        assertFalse(publishingHouse.getIsEnabled());
    }
}
