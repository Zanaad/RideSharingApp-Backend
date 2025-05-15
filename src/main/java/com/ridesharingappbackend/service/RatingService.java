package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    public List<RatingDTO> getAllRatings(Integer companyId) {
        return ratingRepository.findByCompanyId(companyId).stream()
                .map(ratingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RatingDTO> getRatingById(Integer id, Integer companyId) {
        return ratingRepository.findById(id)
                .filter(r -> r.getCompany().getId().equals(companyId))
                .map(ratingMapper::toDTO);
    }

    public List<RatingDTO> getRatingsByCompanyAndDriverId(Integer companyId, Integer driverId) {
        return ratingRepository.findByCompanyIdAndDriverId(companyId, driverId).stream()
                .map(ratingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RatingDTO> getRatingByCompanyAndRideId(Integer companyId, Integer rideId) {
        return ratingRepository.findByCompanyIdAndRideId(companyId, rideId)
                .map(ratingMapper::toDTO);
    }

    public RatingDTO createRating(RatingDTO dto, Integer companyId) {
        Rating rating = ratingMapper.toEntity(dto);
        return ratingMapper.toDTO(ratingRepository.save(rating));
    }

    public boolean deleteRatingByIdAndCompanyId(Integer id, Integer companyId) {
        return ratingRepository.findById(id)
                .filter(r -> r.getCompany().getId().equals(companyId))
                .map(r -> {
                    ratingRepository.delete(r);
                    return true;
                })
                .orElse(false);
    }
}
