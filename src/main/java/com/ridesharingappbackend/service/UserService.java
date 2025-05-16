package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsersByCompany(Integer companyId) {
        return userRepository.findByCompanyId(companyId).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Integer id, Integer companyId) {
        return userRepository.findByIdAndCompanyId(id, companyId)
                .map(userMapper::toDTO);
    }

    public UserDTO updateUser(Integer id, UserDTO updatedUserDto, Integer companyId) {
        return userRepository.findByIdAndCompanyId(id, companyId)
                .map(user -> {
                    userMapper.updateUserFromDto(updatedUserDto, user);
                    return userMapper.toDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean deleteUser(Integer id, Integer companyId) {
        return userRepository.findByIdAndCompanyId(id, companyId)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    public void changePassword(Integer userId, Integer companyId, String currentPassword, String newPassword) {
        var user = userRepository.findByIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}