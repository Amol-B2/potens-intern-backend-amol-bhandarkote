package com.apartmentrecommendation.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.entity.Apartment;

class EligibilityServiceTest {

    private EligibilityService eligibilityService;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityService();
    }

    @Test
    void shouldAcceptApartmentWhenRentExactlyMatchesBudget() {
        Apartment apartment = baseApartment();
        apartment.setRentMonthly(new BigDecimal("1800.00"));

        RecommendationRequest profile = baseProfile();
        profile.setBudgetMax(new BigDecimal("1800.00"));

        assertTrue(eligibilityService.isWithinBudget(apartment, profile));
        assertTrue(eligibilityService.isEligible(apartment, profile));
    }

    @Test
    void shouldAcceptApartmentWhenOccupancyExactlyMatchesFamilySize() {
        Apartment apartment = baseApartment();
        apartment.setMaxOccupancy(3);

        RecommendationRequest profile = baseProfile();
        profile.setFamilySize(3);

        assertTrue(eligibilityService.hasEnoughCapacity(apartment, profile));
        assertTrue(eligibilityService.isEligible(apartment, profile));
    }

    @Test
    void shouldSkipIncomeCheckWhenIncomeIsMissing() {
        Apartment apartment = baseApartment();
        apartment.setMinIncomeRequired(new BigDecimal("7000.00"));

        RecommendationRequest profile = baseProfile();
        profile.setIncomeMonthly(null);

        assertTrue(eligibilityService.satisfiesIncomeRequirement(apartment, profile));
        assertTrue(eligibilityService.isEligible(apartment, profile));
    }

    @Test
    void shouldRejectNonPetFriendlyApartmentWhenProfileHasPets() {
        Apartment apartment = baseApartment();
        apartment.setPetFriendly(false);

        RecommendationRequest profile = baseProfile();
        profile.setHasPets(true);

        assertFalse(eligibilityService.satisfiesPetPolicy(apartment, profile));
        assertFalse(eligibilityService.isEligible(apartment, profile));
    }

    @Test
    void shouldRejectApartmentWhenCityDoesNotMatch() {
        Apartment apartment = baseApartment();
        apartment.setCity("Seattle");

        RecommendationRequest profile = baseProfile();
        profile.setCity("Austin");

        assertFalse(eligibilityService.cityMatches(apartment, profile));
        assertFalse(eligibilityService.isEligible(apartment, profile));
    }

    @Test
    void shouldRejectApartmentWhenAvailableAfterMoveInDate() {
        Apartment apartment = baseApartment();
        apartment.setAvailableFrom(LocalDate.of(2026, 8, 15));

        RecommendationRequest profile = baseProfile();
        profile.setMoveInDate(LocalDate.of(2026, 8, 1));

        assertFalse(eligibilityService.isAvailableInTime(apartment, profile));
        assertFalse(eligibilityService.isEligible(apartment, profile));
    }

    private Apartment baseApartment() {
        Apartment apartment = new Apartment();
        apartment.setId("apt_test");
        apartment.setTitle("Test Apartment");
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
