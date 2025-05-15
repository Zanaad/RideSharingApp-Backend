package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Company;
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
    void getAllRatings_WithCompanyId_ReturnsFilteredList() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyId(1)).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getAllRatings(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllRatings_WithoutCompanyId_ReturnsAll() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findAll()).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getAllRatings(null);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRatingById_ReturnsDTO() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRatingByIdAndCompanyId_WhenMatch_ReturnsDTO() {
        Company company = new Company(); company.setId(10);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingByIdAndCompanyId(1, 10);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRatingsByCompanyAndDriverId_ReturnsList() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyIdAndDriverId(1, 2)).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getRatingsByCompanyAndDriverId(1, 2);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRatingByCompanyAndRideId_ReturnsDTO() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyIdAndRideId(1, 2)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingByCompanyAndRideId(1, 2);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void createRating_SavesAndReturnsDTO() {
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

        boolean result = ratingService.deleteRating(1);

        assertTrue(result);
        verify(ratingRepository).deleteById(1);
    }

    @Test
    void deleteRating_WhenNotExists_ReturnsFalse() {
        when(ratingRepository.existsById(1)).thenReturn(false);

        boolean result = ratingService.deleteRating(1);

        assertFalse(result);
        verify(ratingRepository, never()).deleteById(any());
    }

    @Test
    void deleteRatingByIdAndCompanyId_WhenMatch_ReturnsTrue() {
        Company company = new Company(); company.setId(10);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRatingByIdAndCompanyId(1, 10);

        assertTrue(result);
        verify(ratingRepository).delete(rating);
    }

    @Test
    void deleteRatingByIdAndCompanyId_WhenNoMatch_ReturnsFalse() {
        Company company = new Company(); company.setId(5);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRatingByIdAndCompanyId(1, 10);

        assertFalse(result);
        verify(ratingRepository, never()).delete(any());
    }
}
