package com.apartmentrecommendation.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.apartmentrecommendation.config.CacheConfig;
import com.apartmentrecommendation.dto.ApartmentMatchResponse;
import com.apartmentrecommendation.dto.ItemExplanationResponse;
import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.dto.RecommendationResponse;
import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.exception.ResourceNotFoundException;
import com.apartmentrecommendation.repository.ApartmentRepository;

@Service
public class RecommendationService {

    private final ApartmentRepository apartmentRepository;
    private final EligibilityService eligibilityService;
    private final ScoringService scoringService;
    private final ExplanationService explanationService;

    public RecommendationService(
            ApartmentRepository apartmentRepository,
            EligibilityService eligibilityService,
            ScoringService scoringService,
            ExplanationService explanationService) {
        this.apartmentRepository = apartmentRepository;
        this.eligibilityService = eligibilityService;
        this.scoringService = scoringService;
        this.explanationService = explanationService;
    }

    @Cacheable(
            cacheNames = CacheConfig.RECOMMENDATIONS_CACHE,
            key = "T(com.apartmentrecommendation.config.RecommendationCacheKey).from(#profile)")
    public RecommendationResponse recommend(RecommendationRequest profile) {
        List<ApartmentMatchResponse> matches = apartmentRepository.findAll().stream()
                .filter(apartment -> eligibilityService.isEligible(apartment, profile))
                .map(apartment -> toScoredMatch(apartment, profile))
                .sorted(Comparator.comparing(ApartmentMatchResponse::getMatchScore).reversed()
                        .thenComparing(ApartmentMatchResponse::getRentMonthly))
                .limit(3)
                .toList();

        return new RecommendationResponse(matches);
    }

    private ApartmentMatchResponse toScoredMatch(Apartment apartment, RecommendationRequest profile) {
        ScoringService.ScoreBreakdown scoreBreakdown = scoringService.calculateScore(apartment, profile);

        ApartmentMatchResponse match = new ApartmentMatchResponse();
        match.setId(apartment.getId());
        match.setTitle(apartment.getTitle());
        match.setCity(apartment.getCity());
        match.setRentMonthly(apartment.getRentMonthly());
        match.setMaxOccupancy(apartment.getMaxOccupancy());
        match.setAvailableFrom(apartment.getAvailableFrom());
        match.setPetFriendly(apartment.getPetFriendly());
        match.setAmenities(apartment.getAmenities());
        match.setMinIncomeRequired(apartment.getMinIncomeRequired());
        match.setDescription(apartment.getDescription());
        match.setMatchScore(scoreBreakdown.totalScore());
        match.setReason(explanationService.buildMatchReason(apartment, profile, scoreBreakdown));
        return match;
    }

    public ItemExplanationResponse explainItem(String itemId) {
        Apartment apartment = apartmentRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found for id: " + itemId));

        return new ItemExplanationResponse(apartment.getId(), explanationService.buildItemExplanation(apartment));
    }
}
