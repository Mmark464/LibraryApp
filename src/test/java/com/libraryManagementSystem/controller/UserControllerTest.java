package com.libraryManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.service.impl.UserServiceImpl;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private static ObjectMapper objectMapper;

    private final Long id = 1L;
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName("Name");

        userDto2 = new UserDto();
        userDto2.setId(id + 1);
        userDto2.setFirstName("New Name");
    }

    @Test
    @WithMockUser
    void getAllUsers() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        String pageSort = "id,asc";  // Сортировка как строка

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort.split(",")));
        List<UserDto> userDtoList = Arrays.asList(userDto, userDto2);
        Page<UserDto> userDtoPage = new PageImpl<>(userDtoList, pageable, userDtoList.size());

        when(userService.getAllUsers(anyInt(), anyInt(), any(String[].class))).thenReturn(userDtoPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/user")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("pageSort", pageSort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$.content[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.content[1].id").value(userDto2.getId()))
                .andExpect(jsonPath("$.content[1].firstName").value(userDto2.getFirstName()));
    }


    @Test
    @WithMockUser
    void getUserById() throws Exception {
        when(userService.getUserById(id)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/user/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()));
    }

    @Test
    @WithMockUser
    void updateUser() throws Exception {
        when(userService.updateUser(userDto)).thenReturn(userDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(userDto2.getFirstName()));
    }

    @Test
    @WithMockUser
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/user/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void changePassword() throws Exception {
        String newPassword = "password";
        String newPasswordRepeat = "password";
        doNothing().when(userService).changePassword(id, newPassword, newPasswordRepeat);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/user/" + id + "/change-password")
                        .param("newPassword", newPassword)
                        .param("newPasswordRepeat", newPasswordRepeat)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
