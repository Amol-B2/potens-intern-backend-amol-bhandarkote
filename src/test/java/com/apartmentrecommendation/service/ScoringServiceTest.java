package com.apartmentrecommendation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.entity.Apartment;

class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
    }

    @Test
    void shouldCalculateWorkedExampleStyleScoreComponents() {
        Apartment apartment = baseApartment();
        apartment.setRentMonthly(new BigDecimal("1700.00"));
        apartment.setMaxOccupancy(4);
        apartment.setAmenities(List.of("parking", "gym", "laundry"));

        RecommendationRequest profile = baseProfile();

        ScoringService.ScoreBreakdown score = scoringService.calculateScore(apartment, profile);

        assertEquals(new BigDecimal("0.944444"), score.budgetFit());
        assertEquals(new BigDecimal("1.000000"), score.amenityOverlap());
        assertEquals(new BigDecimal("0.333333"), score.spaceBuffer());
        assertEquals(new BigDecimal("0.844444"), score.totalScore());
    }

    @Test
    void shouldReturnFullAmenityScoreWhenNoAmenitiesAreRequested() {
        Apartment apartment = baseApartment();

        RecommendationRequest profile = baseProfile();
        profile.setAmenitiesWanted(List.of());

        assertEquals(new BigDecimal("1.000000"), scoringService.calculateAmenityOverlap(apartment, profile));
    }

    @Test
    void shouldTreatAmenityNamesCaseInsensitively() {
        Apartment apartment = baseApartment();
        apartment.setAmenities(List.of("Parking", "Gym", "Laundry"));

        RecommendationRequest profile = baseProfile();
        profile.setAmenitiesWanted(List.of("parking", "GYM"));

        assertEquals(new BigDecimal("1.000000"), scoringService.calculateAmenityOverlap(apartment, profile));
    }

    @Test
    void shouldPreferHigherOverallScoreInsteadOfCheapestApartment() {
        Apartment strongerMatch = baseApartment();
        strongerMatch.setRentMonthly(new BigDecimal("1700.00"));
        strongerMatch.setMaxOccupancy(4);
        strongerMatch.setAmenities(List.of("parking", "gym", "laundry"));

        Apartment cheaperMatch = baseApartment();
        cheaperMatch.setRentMonthly(new BigDecimal("1500.00"));
        cheaperMatch.setMaxOccupancy(5);
        cheaperMatch.setAmenities(List.of("gym"));

        RecommendationRequest profile = baseProfile();

        BigDecimal strongerScore = scoringService.calculateScore(strongerMatch, profile).totalScore();
        BigDecimal cheaperScore = scoringService.calculateScore(cheaperMatch, profile).totalScore();

        assertTrue(strongerScore.compareTo(cheaperScore) > 0);
    }

    private Apartment baseApartment() {
        Apartment apartment = new Apartment();
        apartment.setId("apt_score");
        apartment.setTitle("Scoring Test Apartment");
        apartment.setCity("Austin");
        apartment.setRentMonthly(new BigDecimal("1700.00"));
        apartment.setMaxOccupancy(4);
        apartment.setAvailableFrom(LocalDate.of(2026, 7, 15));
        apartment.setPetFriendly(true);
        apartment.setAmenities(List.of("parking", "gym"));
        apartment.setMinIncomeRequired(new BigDecimal("5100.00"));
        apartment.setDescription("Test listing");
        return apartment;
    }

    private RecommendationRequest baseProfile() {
        RecommendationRequest profile = new RecommendationRequest();
        profile.setBudgetMax(new BigDecimal("1800.00"));
        profile.setFamilySize(3);
        profile.setCity("Austin");
        profile.setMoveInDate(LocalDate.of(2026, 8, 1));
        profile.setHasPets(true);
        profile.setAmenitiesWanted(List.of("parking", "gym"));
        profile.setIncomeMonthly(new BigDecimal("6000.00"));
        return profile;
    }
}
