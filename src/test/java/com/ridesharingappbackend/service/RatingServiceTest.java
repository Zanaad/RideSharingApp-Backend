package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRatings_ReturnsListOfDTOs() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findAll()).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getAllRatings();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRatingById_WhenExists_ReturnsDTO() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRatingById_WhenNotExists_ReturnsEmptyOptional() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        Optional<RatingDTO> result = ratingService.getRatingById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void createRating_SavesEntity_ReturnsDTO() {
        RatingDTO dto = new RatingDTO(); dto.setId(1);
        Rating entity = new Rating(); entity.setId(1);

        when(ratingMapper.toEntity(dto)).thenReturn(entity);
        when(ratingRepository.save(entity)).thenReturn(entity);
        when(ratingMapper.toDTO(entity)).thenReturn(dto);

        RatingDTO result = ratingService.createRating(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void deleteRating_WhenExists_ReturnsTrue() {
        when(ratingRepository.existsById(1)).thenReturn(true);
        doNothing().when(ratingRepository).deleteById(1);

        boolean result = ratingService.deleteRating(1);

        assertTrue(result);
        verify(ratingRepository).deleteById(1);
    }

    @Test
    void deleteRating_WhenNotExists_ReturnsFalse() {
        when(ratingRepository.existsById(2)).thenReturn(false);

        boolean result = ratingService.deleteRating(2);

        assertFalse(result);
        verify(ratingRepository, never()).deleteById(any());
    }
}
