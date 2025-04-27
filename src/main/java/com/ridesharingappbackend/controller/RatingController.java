package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.models.Rating;
import com.ridesharingappbackend.service.RatingService;
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
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Optional<Rating> getRatingById(@PathVariable Integer id) {
        return ratingService.getRatingById(id);
    }

    @PostMapping
    public Rating createRating(@RequestBody Rating rating) {
        return ratingService.createRating(rating);
    }

    @DeleteMapping("/{id}")
    public boolean deleteRating(@PathVariable Integer id) {
        return ratingService.deleteRating(id);
    }
}
