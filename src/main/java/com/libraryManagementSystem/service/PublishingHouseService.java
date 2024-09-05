package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;

public interface PublishingHouseService {
    PublishingHouseDto savePublishingHouse(PublishingHouseRequest publishingHouseRequest);

    Page<PublishingHouseDto> getAllPublishingHouses(int pageNumber, int pageSize);

    PublishingHouseDto getPublishingHouseById(Long id);

    PublishingHouseDto updatePublishingHouse(PublishingHouseDto publishingHouse);

    void deletePublishingHouse(Long id);
}
