package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsMappedDTOList() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        UserDTO dto1 = new UserDTO();
        dto1.setId(1);
        UserDTO dto2 = new UserDTO();
        dto2.setId(2);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDTO(user1)).thenReturn(dto1);
        when(userMapper.toDTO(user2)).thenReturn(dto2);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(userMapper).toDTO(user1);
        verify(userMapper).toDTO(user2);
    }

    @Test
    void getUserById_WhenExists_ReturnsDTO() {
        User user = new User();
        user.setId(1);
        UserDTO dto = new UserDTO();
        dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        Optional<UserDTO> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(userRepository).findById(1);
        verify(userMapper).toDTO(user);
    }

    @Test
    void updateUser_WhenExists_ReturnsUpdatedDTO() {
        User user = new User();
        user.setId(1);
        UserDTO dto = new UserDTO();
        dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(dto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.updateUser(1, dto);

        assertEquals(1, result.getId());
        verify(userRepository).findById(1);
        verify(userMapper).updateUserFromDto(dto, user);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }

    @Test
    void updateUser_WhenNotExists_ThrowsException() {
        UserDTO dto = new UserDTO();

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(99, dto));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void deleteUser_WhenExists_ReturnsTrue() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_WhenNotExists_ReturnsFalse() {
        when(userRepository.existsById(2)).thenReturn(false);

        boolean result = userService.deleteUser(2);

        assertFalse(result);
        verify(userRepository, never()).deleteById(any());
    }
}
