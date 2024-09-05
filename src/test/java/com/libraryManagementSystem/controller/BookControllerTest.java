package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.dto.BookRequest;
import com.libraryManagementSystem.service.impl.BookServiceImpl;

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
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private final int pageSize = 10;
    private final int pageNumber = 0;
    private final String[] pageSort = {"id", "asc"};
    private BookRequest bookRequest;
    private BookDto bookDto;
    private BookDto bookDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        bookRequest = new BookRequest();
        bookRequest.setTitle("Book Title");

        bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(bookRequest.getTitle());

        bookDto2 = new BookDto();
        bookDto2.setId(id + 1);
        bookDto2.setTitle("New " + bookRequest.getTitle());
    }

    @Test
    @WithMockUser
    void saveBook() throws Exception {
        when(bookService.saveBook(bookRequest)).thenReturn(bookDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser
    void getAllBooks() throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        List<BookDto> bookDtoList = Arrays.asList(bookDto, bookDto2);
        Page<BookDto> bookDtoPage = new PageImpl<>(bookDtoList, pageable, bookDtoList.size());

        when(bookService.getAllBooks(any(int.class), any(int.class), any(String[].class))).thenReturn(bookDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/book")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.content[1].id").value(id + 1))
                .andExpect(jsonPath("$.content[1].title").value(bookDto2.getTitle()));
    }

    @Test
    @WithMockUser
    void getBookById() throws Exception {
        when(bookService.getBookById(id)).thenReturn(bookDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/book/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()));
    }

    @Test
    @WithMockUser
    void bookFilter() throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<BookDto> bookDtoList = Arrays.asList(bookDto, bookDto2);
        Page<BookDto> bookDtoPage = new PageImpl<>(bookDtoList, pageable, bookDtoList.size());

        when(bookService.bookFilter(any(Long.class), anySet(), anySet(), any(String.class), any(int.class), any(int.class), any(String[].class))).thenReturn(bookDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/book/filter")
                        .param("publishingHouseId", "1")
                        .param("authorIds", "1", "2")
                        .param("genreIds", "1", "2")
                        .param("title", "Book Title")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.content[1].id").value(id + 1))
                .andExpect(jsonPath("$.content[1].title").value(bookDto2.getTitle()));
    }

    @Test
    @WithMockUser
    void updateBook() throws Exception {
        when(bookService.updateBook(bookDto)).thenReturn(bookDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id + 1))
                .andExpect(jsonPath("$.title").value(bookDto2.getTitle()));
    }

    @Test
    @WithMockUser
    void deleteBookById() throws Exception {
        doNothing().when(bookService).deleteBook(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/book/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
