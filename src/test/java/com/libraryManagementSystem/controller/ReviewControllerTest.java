package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.ReviewDto;
import com.libraryManagementSystem.dto.ReviewRequest;
import com.libraryManagementSystem.service.impl.ReviewServiceImpl;
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
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private ReviewRequest reviewRequest;
    private ReviewDto reviewDto;
    private ReviewDto reviewDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        reviewRequest = new ReviewRequest();
        reviewRequest.setComment("comment");

        reviewDto = new ReviewDto();
        reviewDto.setId(id);
        reviewDto.setComment(reviewRequest.getComment());

        reviewDto2 = new ReviewDto();
        reviewDto2.setId(id + 1);
        reviewDto2.setComment("New " + reviewRequest.getComment());
    }

    @Test
    @WithMockUser
    void saveReview() throws Exception {
        when(reviewService.saveReview(reviewRequest)).thenReturn(reviewDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.comment").value(reviewRequest.getComment()));
    }

    @Test
    @WithMockUser
    void getAllReviews() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        List<ReviewDto> reviewDtoList = Arrays.asList(reviewDto, reviewDto2);
        Page<ReviewDto> reviewDtoPage = new PageImpl<>(reviewDtoList, pageable, reviewDtoList.size());

        when(reviewService.getAllReviews(any(int.class), any(int.class), any(String[].class))).thenReturn(reviewDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/review")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].comment").value(reviewRequest.getComment()))
                .andExpect(jsonPath("$.content[1].id").value(reviewDto2.getId()))
                .andExpect(jsonPath("$.content[1].comment").value(reviewDto2.getComment()));
    }

    @Test
    @WithMockUser
    void getReviewById() throws Exception {
        when(reviewService.getReviewById(id)).thenReturn(reviewDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/review/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.comment").value(reviewRequest.getComment()));
    }

    @Test
    @WithMockUser
    void updateReview() throws Exception {
        when(reviewService.updateReview(reviewDto)).thenReturn(reviewDto2);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(reviewDto2.getId()))
                .andExpect(jsonPath("$.comment").value(reviewDto2.getComment()));
    }

    @Test
    @WithMockUser
    void deleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/review/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
