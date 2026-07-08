package com.apartmentrecommendation.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.apartmentrecommendation.dto.RecommendationRequest;

public record RecommendationCacheKey(
        BigDecimal budgetMax,
        Integer familySize,
        String city,
        LocalDate moveInDate,
        Boolean hasPets,
        List<String> amenitiesWanted,
        BigDecimal incomeMonthly) {

    public static RecommendationCacheKey from(RecommendationRequest request) {
        List<String> normalizedAmenities = request.getAmenitiesWanted() == null
                ? List.of()
                : request.getAmenitiesWanted().stream()
                        .filter(amenity -> amenity != null && !amenity.isBlank())
                        .map(amenity -> amenity.trim().toLowerCase())
                        .sorted()
                        .toList();

        return new RecommendationCacheKey(
                normalize(request.getBudgetMax()),
                request.getFamilySize(),
                normalize(request.getCity()),
                request.getMoveInDate(),
                request.getHasPets(),
                normalizedAmenities,
                normalize(request.getIncomeMonthly()));
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    private static BigDecimal normalize(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros();
    }
}
