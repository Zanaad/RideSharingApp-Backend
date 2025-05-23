package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.RatingRepository;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository,
                         RatingMapper ratingMapper,
                         CompanyRepository companyRepository,
                         UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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

    public RatingDTO createRating(RatingDTO dto, Integer companyId, Integer passengerId) {
        Rating rating = ratingMapper.toEntity(dto);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID"));
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger ID"));

        rating.setCompany(company);
        rating.setPassenger(passenger);

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

    public List<RatingDTO> getRatingsForDriver(Integer driverId) {
        List<Rating> ratingList = ratingRepository.findByDriverId(driverId);
        return ratingList.stream().map(ratingMapper::toDTO).toList();
    }

}