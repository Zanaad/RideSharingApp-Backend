package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.mappers.RatingMapper;
import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Shërbimi që menaxhon logjikën e biznesit për entitetin {@link Rating}.
 *
 * Ky shërbim ofron operacione për:
 * <ul>
 *     <li>Marrjen e të gjitha vlerësimeve për një kompani të caktuar</li>
 *     <li>Gjetjen e një vlerësimi sipas ID-së dhe ID-së së kompanisë</li>
 *     <li>Marrjen e vlerësimeve për një shofer të caktuar dhe kompani</li>
 *     <li>Gjetjen e një vlerësimi për një udhëtim specifik dhe kompani</li>
 *     <li>Krijimin e një vlerësimi të ri</li>
 *     <li>Fshirjen e një vlerësimi duke verifikuar pronësinë e kompanisë</li>
 *     <li>Marrjen e të gjitha vlerësimeve për një shofer të caktuar</li>
 * </ul>
 *
 * Ky shërbim përdor {@link RatingRepository} për të komunikuar me bazën e të dhënave
 * dhe {@link RatingMapper} për të transformuar të dhënat midis {@link Rating} dhe {@link RatingDTO}.
 *
 * @author [Emri juaj]
 * @since 2025-05-22
 * @version 1.0
 */

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
    public List<RatingDTO> getRatingsForDriver(Integer driverId) {
        List<Rating> ratingList = ratingRepository.findByDriverId(driverId);
        return ratingList.stream().map(ratingMapper::toDTO).toList();
    }

}
