package com.apartmentrecommendation.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.entity.Apartment;

@Service
public class EligibilityService {

    public boolean isEligible(Apartment apartment, RecommendationRequest profile) {
        return cityMatches(apartment, profile)
                && isWithinBudget(apartment, profile)
                && hasEnoughCapacity(apartment, profile)
                && isAvailableInTime(apartment, profile)
                && satisfiesPetPolicy(apartment, profile)
                && satisfiesIncomeRequirement(apartment, profile);
    }

    public boolean cityMatches(Apartment apartment, RecommendationRequest profile) {
        return apartment.getCity() != null
                && profile.getCity() != null
                && apartment.getCity().equalsIgnoreCase(profile.getCity().trim());
    }

    public boolean isWithinBudget(Apartment apartment, RecommendationRequest profile) {
        return compare(apartment.getRentMonthly(), profile.getBudgetMax()) <= 0;
    }

    public boolean hasEnoughCapacity(Apartment apartment, RecommendationRequest profile) {
        return apartment.getMaxOccupancy() != null
                && profile.getFamilySize() != null
                && apartment.getMaxOccupancy() >= profile.getFamilySize();
    }

    public boolean isAvailableInTime(Apartment apartment, RecommendationRequest profile) {
        return apartment.getAvailableFrom() != null
                && profile.getMoveInDate() != null
                && !apartment.getAvailableFrom().isAfter(profile.getMoveInDate());
    }

    public boolean satisfiesPetPolicy(Apartment apartment, RecommendationRequest profile) {
        if (!Boolean.TRUE.equals(profile.getHasPets())) {
            return true;
        }

        return Boolean.TRUE.equals(apartment.getPetFriendly());
    }

    public boolean satisfiesIncomeRequirement(Apartment apartment, RecommendationRequest profile) {
        if (profile.getIncomeMonthly() == null) {
            return true;
        }

        return compare(profile.getIncomeMonthly(), apartment.getMinIncomeRequired()) >= 0;
    }

    private int compare(BigDecimal left, BigDecimal right) {
        if (left == null || right == null) {
            return Integer.MIN_VALUE;
        }

        return left.compareTo(right);
    }
}
