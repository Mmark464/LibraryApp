package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.PublishingHouseDto;
import com.libraryManagementSystem.dto.PublishingHouseRequest;
import com.libraryManagementSystem.service.impl.PublishingHouseServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PublishingHouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublishingHouseServiceImpl publishingHouseService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private PublishingHouseRequest publishingHouseRequest;
    private PublishingHouseDto publishingHouseDto;
    private PublishingHouseDto publishingHouseDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        publishingHouseRequest = new PublishingHouseRequest();
        publishingHouseRequest.setName("Name");

        publishingHouseDto = new PublishingHouseDto();
        publishingHouseDto.setId(id);
        publishingHouseDto.setName(publishingHouseRequest.getName());

        publishingHouseDto2 = new PublishingHouseDto();
        publishingHouseDto2.setId(id + 1);
        publishingHouseDto2.setName("New " + publishingHouseRequest.getName());
    }

    @Test
    @WithMockUser
    void savePublishingHouse() throws Exception {
        when(publishingHouseService.savePublishingHouse(publishingHouseRequest)).thenReturn(publishingHouseDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/publishing-house")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishingHouseRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(publishingHouseRequest.getName()));
    }

    @Test
    @WithMockUser
    void getAllPublishingHouses() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        List<PublishingHouseDto> publishingHouseDtoList = Arrays.asList(publishingHouseDto, publishingHouseDto2);
        Page<PublishingHouseDto> publishingHouseDtoPage = new PageImpl<>(publishingHouseDtoList, pageable, publishingHouseDtoList.size());

        when(publishingHouseService.getAllPublishingHouses(any(int.class), any(int.class), any(String[].class))).thenReturn(publishingHouseDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/publishing-house")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(publishingHouseDto.getId()))
                .andExpect(jsonPath("$.content[0].name").value(publishingHouseDto.getName()))
                .andExpect(jsonPath("$.content[1].id").value(publishingHouseDto2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(publishingHouseDto2.getName()));
    }

    @Test
    @WithMockUser
    void getPublishingHouseById() throws Exception {
        when(publishingHouseService.getPublishingHouseById(id)).thenReturn(publishingHouseDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/publishing-house/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishingHouseDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(publishingHouseRequest.getName()));
    }

    @Test
    @WithMockUser
    void updatePublishingHouse() throws Exception {
        when(publishingHouseService.updatePublishingHouse(publishingHouseDto)).thenReturn(publishingHouseDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/publishing-house")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishingHouseDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(publishingHouseDto2.getId()))
                .andExpect(jsonPath("$.name").value(publishingHouseDto2.getName()));
    }

    @Test
    @WithMockUser
    void deletePublishingHouse() throws Exception {
        doNothing().when(publishingHouseService).deletePublishingHouse(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/publishing-house/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishingHouseDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
