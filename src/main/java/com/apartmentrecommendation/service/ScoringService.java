package com.apartmentrecommendation.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.entity.Apartment;

@Service
public class ScoringService {

    private static final BigDecimal BUDGET_WEIGHT = new BigDecimal("0.4");
    private static final BigDecimal AMENITY_WEIGHT = new BigDecimal("0.4");
    private static final BigDecimal SPACE_WEIGHT = new BigDecimal("0.2");
    private static final int SCALE = 6;

    public ScoreBreakdown calculateScore(Apartment apartment, RecommendationRequest profile) {
        BigDecimal budgetFit = calculateBudgetFit(apartment, profile);
        BigDecimal amenityOverlap = calculateAmenityOverlap(apartment, profile);
        BigDecimal spaceBuffer = calculateSpaceBuffer(apartment, profile);

        BigDecimal totalScore = budgetFit.multiply(BUDGET_WEIGHT)
                .add(amenityOverlap.multiply(AMENITY_WEIGHT))
                .add(spaceBuffer.multiply(SPACE_WEIGHT))
                .setScale(SCALE, RoundingMode.HALF_UP);

        return new ScoreBreakdown(budgetFit, amenityOverlap, spaceBuffer, totalScore);
    }

    public BigDecimal calculateBudgetFit(Apartment apartment, RecommendationRequest profile) {
        BigDecimal budgetMax = profile.getBudgetMax();
        BigDecimal rentMonthly = apartment.getRentMonthly();

        if (budgetMax == null || rentMonthly == null || budgetMax.signum() <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal gap = budgetMax.subtract(rentMonthly);
        BigDecimal ratio = gap.divide(budgetMax, SCALE, RoundingMode.HALF_UP);
        BigDecimal budgetFit = BigDecimal.ONE.subtract(ratio);

        return clamp(budgetFit);
    }

    public BigDecimal calculateAmenityOverlap(Apartment apartment, RecommendationRequest profile) {
        List<String> wantedAmenities = profile.getAmenitiesWanted();
        if (wantedAmenities == null || wantedAmenities.isEmpty()) {
            return BigDecimal.ONE.setScale(SCALE, RoundingMode.HALF_UP);
        }

        Set<String> availableAmenities = normalize(apartment.getAmenities());
        Set<String> wantedNormalized = normalize(wantedAmenities);

        long matches = wantedNormalized.stream()
                .filter(availableAmenities::contains)
                .count();

        return BigDecimal.valueOf(matches)
                .divide(BigDecimal.valueOf(wantedNormalized.size()), SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateSpaceBuffer(Apartment apartment, RecommendationRequest profile) {
        Integer maxOccupancy = apartment.getMaxOccupancy();
        Integer familySize = profile.getFamilySize();

        if (maxOccupancy == null || familySize == null || familySize <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal extraSpace = BigDecimal.valueOf(maxOccupancy - familySize);
        BigDecimal ratio = extraSpace.divide(BigDecimal.valueOf(familySize), SCALE, RoundingMode.HALF_UP);

        return clamp(ratio.min(BigDecimal.ONE));
    }

    private Set<String> normalize(List<String> values) {
        Set<String> normalized = new HashSet<>();
        if (values == null) {
            return normalized;
        }

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                normalized.add(value.trim().toLowerCase());
            }
        }

        return normalized;
    }

    private BigDecimal clamp(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }

        if (value.compareTo(BigDecimal.ONE) > 0) {
            return BigDecimal.ONE.setScale(SCALE, RoundingMode.HALF_UP);
        }

        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public record ScoreBreakdown(
            BigDecimal budgetFit,
            BigDecimal amenityOverlap,
            BigDecimal spaceBuffer,
            BigDecimal totalScore) {
    }
}
