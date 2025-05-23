package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.RatingRepository;
import com.ridesharingappbackend.repository.UserRepository;
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
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private RatingService ratingService;

    private final Integer companyId = 10;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRatings_WithCompanyId_ReturnsFilteredList() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyId(companyId)).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getAllRatings(companyId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRatingByIdAndCompanyId_WhenMatch_ReturnsDTO() {
        Company company = new Company(); company.setId(companyId);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingById(1, companyId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getRatingByIdAndCompanyId_WhenNotMatchingCompany_ReturnsEmpty() {
        Company company = new Company(); company.setId(99);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Optional<RatingDTO> result = ratingService.getRatingById(1, companyId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRatingsByCompanyAndDriverId_ReturnsList() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyIdAndDriverId(companyId, 2)).thenReturn(List.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        List<RatingDTO> result = ratingService.getRatingsByCompanyAndDriverId(companyId, 2);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getRatingByCompanyAndRideId_ReturnsDTO() {
        Rating rating = new Rating(); rating.setId(1);
        RatingDTO dto = new RatingDTO(); dto.setId(1);

        when(ratingRepository.findByCompanyIdAndRideId(companyId, 2)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        Optional<RatingDTO> result = ratingService.getRatingByCompanyAndRideId(companyId, 2);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void createRating_SavesAndReturnsDTO() {
        RatingDTO dto = new RatingDTO();
        dto.setId(1);

        Rating rating = new Rating();
        rating.setId(1);

        Company company = new Company();
        company.setId(companyId);

        User passenger = new User();
        passenger.setId(100); // any sample ID

        when(ratingMapper.toEntity(dto)).thenReturn(rating);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingMapper.toDTO(rating)).thenReturn(dto);

        RatingDTO result = ratingService.createRating(dto, companyId, passenger.getId());

        assertEquals(1, result.getId());
        verify(ratingRepository).save(any(Rating.class));
    }


    @Test
    void deleteRatingByIdAndCompanyId_WhenMatch_ReturnsTrue() {
        Company company = new Company(); company.setId(companyId);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRatingByIdAndCompanyId(1, companyId);

        assertTrue(result);
        verify(ratingRepository).delete(rating);
    }

    @Test
    void deleteRatingByIdAndCompanyId_WhenNoMatch_ReturnsFalse() {
        Company company = new Company(); company.setId(5);
        Rating rating = new Rating(); rating.setId(1); rating.setCompany(company);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRatingByIdAndCompanyId(1, companyId);

        assertFalse(result);
        verify(ratingRepository, never()).delete(any());
    }

    @Test
    void deleteRatingByIdAndCompanyId_WhenNotExists_ReturnsFalse() {
        when(ratingRepository.findById(999)).thenReturn(Optional.empty());

        boolean result = ratingService.deleteRatingByIdAndCompanyId(999, companyId);

        assertFalse(result);
    }
}
