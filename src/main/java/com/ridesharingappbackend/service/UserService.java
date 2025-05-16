package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "users", key = "#companyId")
    public List<UserDTO> getAllUsersByCompany(Integer companyId) {
        System.out.println("Fetching from DB for ID: " + companyId);
        return userRepository.findByCompanyId(companyId).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Integer id, Integer companyId) {
        return userRepository.findById(id)
                .filter(user -> user.getCompany().getId().equals(companyId))
                .map(userMapper::toDTO);
    }

    public UserDTO updateUser(Integer id, UserDTO updatedUserDto, Integer companyId) {
        return userRepository.findById(id)
                .filter(user -> user.getCompany().getId().equals(companyId))
                .map(user -> {
                    userMapper.updateUserFromDto(updatedUserDto, user);
                    return userMapper.toDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found or not in your company"));
    }

    public boolean deleteUser(Integer id, Integer companyId) {
        return userRepository.findById(id)
                .filter(user -> user.getCompany().getId().equals(companyId))
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }
}
