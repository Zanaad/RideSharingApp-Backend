package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public UserDTO updateUser(Integer id, UserDTO updatedUserDto) {
        return userRepository.findById(id).map(user -> {
            userMapper.updateUserFromDto(updatedUserDto, user);
            return userMapper.toDTO(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
