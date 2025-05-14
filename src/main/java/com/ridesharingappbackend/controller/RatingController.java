package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<RatingDTO> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable Integer id) {
        Optional<RatingDTO> rating = ratingService.getRatingById(id);
        return rating.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RatingDTO createRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.createRating(ratingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer id) {
        boolean deleted = ratingService.deleteRating(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
