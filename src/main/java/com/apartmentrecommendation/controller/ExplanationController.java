package com.apartmentrecommendation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.apartmentrecommendation.dto.ItemExplanationResponse;
import com.apartmentrecommendation.service.RecommendationService;

@RestController
public class ExplanationController {

    private final RecommendationService recommendationService;

    public ExplanationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/explain/{itemId}")
    public ItemExplanationResponse explain(@PathVariable String itemId) {
        return recommendationService.explainItem(itemId);
    }
}
