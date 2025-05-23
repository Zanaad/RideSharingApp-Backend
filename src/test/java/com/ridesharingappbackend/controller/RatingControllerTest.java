package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.service.RatingService;
import com.ridesharingappbackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;

class RatingControllerTest {

    @InjectMocks
    private RatingController ratingController;

    @Mock
    private RatingService ratingService;

    @Mock
    private RideService rideService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RatingDTO createSampleRating() {
        return new RatingDTO(
                1,          // id
                100,        // rideId
                200,        // passengerId
                300,        // driverId
                5,          // rating
                "Great ride!", // review
                LocalDateTime.now(), // createdAt
                1           // companyId
        );
    }

    @Test
    void getAllRatings_withCompanyIdParam() {
        RatingDTO rating = createSampleRating();
        when(ratingService.getAllRatings(1)).thenReturn(List.of(rating));

        List<RatingDTO> result = ratingController.getAllRatings(1, request);

        assertEquals(1, result.size());
        assertEquals(rating, result.get(0));
        verify(ratingService).getAllRatings(1);
    }

    @Test
    void getAllRatings_withoutCompanyIdParam_usesRequestAttribute() {
        RatingDTO rating = createSampleRating();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.getAllRatings(1)).thenReturn(List.of(rating));

        List<RatingDTO> result = ratingController.getAllRatings(null, request);

        assertEquals(1, result.size());
        assertEquals(rating, result.get(0));
        verify(ratingService).getAllRatings(1);
    }

    @Test
    void getRatingById_found() {
        RatingDTO rating = createSampleRating();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.getRatingById(1, 1)).thenReturn(Optional.of(rating));

        ResponseEntity<RatingDTO> response = ratingController.getRatingById(1, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(rating, response.getBody());
    }

    @Test
    void getRatingById_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.getRatingById(999, 1)).thenReturn(Optional.empty());

        ResponseEntity<RatingDTO> response = ratingController.getRatingById(999, request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getRatingsByDriver_returnsList() {
        when(request.getAttribute("companyId")).thenReturn(1);
        RatingDTO rating = createSampleRating();
        when(ratingService.getRatingsByCompanyAndDriverId(1, 10)).thenReturn(List.of(rating));

        List<RatingDTO> result = ratingController.getRatingsByDriver(10, request);

        assertEquals(1, result.size());
        assertEquals(rating, result.get(0));
    }

    @Test
    void getRatingByRide_found() {
        when(request.getAttribute("companyId")).thenReturn(1);
        RatingDTO rating = createSampleRating();
        when(ratingService.getRatingByCompanyAndRideId(1, 100)).thenReturn(Optional.of(rating));

        ResponseEntity<RatingDTO> response = ratingController.getRatingByRide(100, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(rating, response.getBody());
    }

    @Test
    void getRatingByRide_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.getRatingByCompanyAndRideId(1, 999)).thenReturn(Optional.empty());

        ResponseEntity<RatingDTO> response = ratingController.getRatingByRide(999, request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createRating_success() {
        RatingDTO input = createSampleRating();
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.createRating(input, 1)).thenReturn(input);

        RatingDTO response = ratingController.createRating(input, request);

        assertEquals(input, response);
    }

    @Test
    void deleteRating_success() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.deleteRatingByIdAndCompanyId(1, 1)).thenReturn(true);

        ResponseEntity<Void> response = ratingController.deleteRating(1, request);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteRating_notFound() {
        when(request.getAttribute("companyId")).thenReturn(1);
        when(ratingService.deleteRatingByIdAndCompanyId(999, 1)).thenReturn(false);

        ResponseEntity<Void> response = ratingController.deleteRating(999, request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getMyRatings_authorized() {
        when(request.getAttribute("userId")).thenReturn(42);
        RatingDTO rating = createSampleRating();
        when(ratingService.getRatingsForDriver(42)).thenReturn(List.of(rating));

        ResponseEntity<List<RatingDTO>> response = ratingController.getMyRatings(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(rating, response.getBody().get(0));
    }

    @Test
    void getMyRatings_unauthorized() {
        when(request.getAttribute("userId")).thenReturn(null);

        ResponseEntity<List<RatingDTO>> response = ratingController.getMyRatings(request);

        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
