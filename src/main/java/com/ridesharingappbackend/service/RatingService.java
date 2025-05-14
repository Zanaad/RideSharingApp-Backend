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

    public List<RatingDTO> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RatingDTO> getRatingById(Integer id) {
        return ratingRepository.findById(id)
                .map(ratingMapper::toDTO);
    }

    public RatingDTO createRating(RatingDTO dto) {
        Rating rating = ratingMapper.toEntity(dto);
        return ratingMapper.toDTO(ratingRepository.save(rating));
    }

    public boolean deleteRating(Integer id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
