package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.RoleDto;
import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.dto.UserRequest;
import com.libraryManagementSystem.entity.Role;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.mapper.UserMapper;
import com.libraryManagementSystem.repository.RoleRepository;
import com.libraryManagementSystem.repository.UserRepository;
import com.libraryManagementSystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final Long id = 1L;
    private UserRequest userRequest;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void init(){
        userRequest = new UserRequest();
        userRequest.setRoles(new HashSet<>());

        user = new User();
        user.setId(id);

        userDto = new UserDto();
        userDto.setId(id);
    }

    @Test
    public void register() {
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn(encodedPassword);

        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_USER");
        userRequest.getRoles().add(roleDto);

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        when(userMapper.requestToEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.entityToDto(user)).thenReturn(userDto);

        UserDto result = userService.register(userRequest);
        assertEquals(userDto, result);
    }

    @Test
    public void getAllUsers() {
        User user2 = new User();

        UserDto userDto2 = new UserDto();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        int pageSize = 10;
        int pageNumber = 0;
        String[] pageSort = {"id", "asc"};
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(pageSort));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAllByIsEnabledTrue(pageable)).thenReturn(userPage);
        when(userMapper.entityToDto(user)).thenReturn(userDto);
        when(userMapper.entityToDto(user2)).thenReturn(userDto2);

        Page<UserDto> result = userService.getAllUsers(pageSize, pageNumber, pageSort);

        assertEquals(userDto, result.getContent().getFirst());
        assertEquals(userDto2, result.getContent().getLast());
    }

    @Test
    public void getUserById() {
        when(userRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(user));
        when(userMapper.entityToDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(id);

        assertEquals(userDto, result);
    }

    @Test
    public void updateUser() {
        User updatedUser = new User();
        updatedUser.setId(id);

        doNothing().when(userMapper).updateEntityFromDto(userDto, user);
        when(userRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.entityToDto(updatedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(userDto);

        assertEquals(userDto, result);
    }

    @Test
    public void deleteUser() {
        when(userRepository.findByIdAndIsEnabledTrue(id)).thenReturn(Optional.of(user));
        userService.deleteUser(id);

        assertFalse(user.getIsEnabled());
    }
}
