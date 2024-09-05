package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.AuthorDto;
import com.libraryManagementSystem.dto.AuthorRequest;
import com.libraryManagementSystem.service.impl.AuthorServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private AuthorRequest authorRequest;
    private AuthorDto authorDto;
    private AuthorDto authorDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        authorRequest = new AuthorRequest();
        authorRequest.setFirstName("firstName");

        authorDto = new AuthorDto();
        authorDto.setId(id);
        authorDto.setFirstName(authorRequest.getFirstName());

        authorDto2 = new AuthorDto();
        authorDto2.setId(id + 1);
        authorDto2.setFirstName("New " + authorRequest.getFirstName());
    }

    @Test
    @WithMockUser
    void saveAuthor() throws Exception {
        when(authorService.saveAuthor(authorRequest)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser
    void getAllAuthors() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        List<AuthorDto> authorDtoList = Arrays.asList(authorDto, authorDto2);
        Page<AuthorDto> authorDtoPage = new PageImpl<>(authorDtoList, pageable, authorDtoList.size());

        when(authorService.getAllAuthors(any(int.class), any(int.class), any(String[].class))).thenReturn(authorDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/author")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].firstName").value(authorDto.getFirstName()))
                .andExpect(jsonPath("$.content[1].id").value(id + 1))
                .andExpect(jsonPath("$.content[1].firstName").value(authorDto2.getFirstName()));
    }

    @Test
    @WithMockUser
    void getAuthorById() throws Exception {

        when(authorService.getAuthorById(id)).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/author/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(authorDto.getFirstName()));
    }

    @Test
    @WithMockUser
    void updateAuthor() throws Exception {
        when(authorService.updateAuthor(authorDto)).thenReturn(authorDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(authorDto2.getId()))
                .andExpect(jsonPath("$.firstName").value(authorDto2.getFirstName()));
    }

    @Test
    @WithMockUser
    void deleteAuthor() throws Exception {
        doNothing().when(authorService).deleteAuthor(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/author/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
