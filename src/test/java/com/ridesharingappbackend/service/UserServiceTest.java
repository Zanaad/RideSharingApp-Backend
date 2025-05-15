package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private final Integer companyId = 5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsersByCompany_ReturnsMappedDTOList() {
        User user1 = new User(); user1.setId(1);
        User user2 = new User(); user2.setId(2);
        UserDTO dto1 = new UserDTO(); dto1.setId(1);
        UserDTO dto2 = new UserDTO(); dto2.setId(2);

        when(userRepository.findByCompanyId(companyId)).thenReturn(List.of(user1, user2));
        when(userMapper.toDTO(user1)).thenReturn(dto1);
        when(userMapper.toDTO(user2)).thenReturn(dto2);

        List<UserDTO> result = userService.getAllUsersByCompany(companyId);

        assertEquals(2, result.size());
        verify(userRepository).findByCompanyId(companyId);
        verify(userMapper).toDTO(user1);
        verify(userMapper).toDTO(user2);
    }

    @Test
    void getUserById_WhenCompanyMatches_ReturnsDTO() {
        Company company = new Company(); company.setId(companyId);
        User user = new User(); user.setId(1); user.setCompany(company);
        UserDTO dto = new UserDTO(); dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        Optional<UserDTO> result = userService.getUserById(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getUserById_WhenCompanyMismatch_ReturnsEmpty() {
        Company company = new Company(); company.setId(99);
        User user = new User(); user.setId(1); user.setCompany(company);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUserById(1, companyId);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_WhenCompanyMatches_ReturnsUpdatedDTO() {
        Company company = new Company(); company.setId(companyId);
        User user = new User(); user.setId(1); user.setCompany(company);
        UserDTO dto = new UserDTO(); dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(dto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.updateUser(1, dto, companyId);

        assertEquals(1, result.getId());
    }

    @Test
    void updateUser_WhenCompanyMismatch_ThrowsException() {
        Company company = new Company(); company.setId(99);
        User user = new User(); user.setId(1); user.setCompany(company);
        UserDTO dto = new UserDTO();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1, dto, companyId));

        assertEquals("User not found or not in your company", ex.getMessage());
    }

    @Test
    void deleteUser_WhenCompanyMatches_DeletesSuccessfully() {
        Company company = new Company(); company.setId(companyId);
        User user = new User(); user.setId(1); user.setCompany(company);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1, companyId);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_WhenCompanyMismatch_ReturnsFalse() {
        Company company = new Company(); company.setId(99);
        User user = new User(); user.setId(1); user.setCompany(company);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1, companyId);

        assertFalse(result);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_WhenUserNotFound_ReturnsFalse() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1, companyId);

        assertFalse(result);
    }
}
