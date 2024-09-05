package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.GenreDto;
import com.libraryManagementSystem.dto.GenreRequest;
import com.libraryManagementSystem.service.impl.GenreServiceImpl;
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
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreServiceImpl genreService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private GenreRequest genreRequest;
    private GenreDto genreDto;
    private GenreDto genreDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        genreRequest = new GenreRequest();
        genreRequest.setName("Genre");

        genreDto = new GenreDto();
        genreDto.setId(id);
        genreDto.setName(genreRequest.getName());

        genreDto2 = new GenreDto();
        genreDto2.setId(id + 1);
        genreDto2.setName("New " + genreRequest.getName());
    }

    @Test
    @WithMockUser
    void saveGenre() throws Exception {
        when(genreService.saveGenre(genreRequest)).thenReturn(genreDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(genreRequest.getName()));
    }

    @Test
    @WithMockUser
    void getAllGenres() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        List<GenreDto> genreDtoList = Arrays.asList(genreDto, genreDto2);
        Page<GenreDto> genreDtoPage = new PageImpl<>(genreDtoList, pageable, genreDtoList.size());

        when(genreService.getAllGenres(any(int.class), any(int.class), any(String[].class))).thenReturn(genreDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/genre")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].name").value(genreDto.getName()))
                .andExpect(jsonPath("$.content[1].id").value(genreDto2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(genreDto2.getName()));
    }

    @Test
    @WithMockUser
    void getGenreById() throws Exception {
        when(genreService.getGenreById(id)).thenReturn(genreDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/genre/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(genreDto.getName()));
    }

    @Test
    @WithMockUser
    void updateGenre() throws Exception {
        when(genreService.updateGenre(genreDto)).thenReturn(genreDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(genreDto2.getId()))
                .andExpect(jsonPath("$.name").value(genreDto2.getName()));
    }

    @Test
    @WithMockUser
    void deleteGenre() throws Exception {
        doNothing().when(genreService).deleteGenre(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/genre/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
