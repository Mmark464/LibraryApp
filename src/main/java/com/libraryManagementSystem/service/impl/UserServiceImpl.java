package com.libraryManagementSystem.service.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.libraryManagementSystem.jwt.JwtService;
import com.libraryManagementSystem.repository.*;
import com.libraryManagementSystem.exception.*;
import org.springframework.cache.annotation.*;
import com.libraryManagementSystem.service.*;
import com.libraryManagementSystem.mapper.*;
import com.libraryManagementSystem.entity.*;
import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public UserDto register(UserRequest userRequest) {

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (var role : userRequest.getRoles()) {
            roles.add(roleRepository.findByName(role.getName()).orElseThrow(
                    () -> new NotFoundException("Role not found with name: " + role.getName())));
        }
        User user = userMapper.requestToEntity(userRequest);
        user.setRoles(roles);
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        User user = userRepository.findByUsernameAndIsEnabledTrue(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return LoginResponse.builder()
                .token(
                        jwtService.generateToken(user)
                )
                .build();
    }

    @Override
    @Cacheable(value = "users", key = "'allUsers'")
    public Page<UserDto> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAllByIsEnabledTrue(pageable)
                .map(userMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "users", key = "'userById_' + #id")
    public UserDto getUserById(Long id) {
        return userMapper.entityToDto(
                findById(id)
        );
    }

    @Override
    @CachePut(value = "users", key = "'userById_' + #user.id")
    public UserDto updateUser(UserDto user) {
        userMapper.updateEntityFromDto(
                user, findById(
                        user.getId()
                ));

        return userMapper.entityToDto(
                userRepository.save(
                        findById(
                                user.getId()
                        )));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "'allUsers'", allEntries = true),
            @CacheEvict(value = "users", key = "'userById_' + #id")
    })
    public void deleteUser(Long id) {
        findById(id).setIsEnabled(false);
        userRepository.save(
                findById(id)
        );
    }

    @Override
    public void changePassword(Long id, String newPassword, String newPasswordRepeat) {

        if (!newPassword.equals(newPasswordRepeat)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        findById(id).setPassword(
                passwordEncoder.encode(newPassword).toCharArray()
        );

        userRepository.save(
                findById(id)
        );
    }

    private User findById(Long id) {
        return userRepository.findByIdAndIsEnabledTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}