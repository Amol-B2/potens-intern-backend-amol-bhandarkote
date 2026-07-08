package com.apartmentrecommendation.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.entity.Apartment;

@Service
public class ExplanationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public String buildMatchReason(
            Apartment apartment,
            RecommendationRequest profile,
            ScoringService.ScoreBreakdown scoreBreakdown) {
        List<String> reasons = new ArrayList<>();

        reasons.add("it falls within your " + formatCurrency(profile.getBudgetMax())
                + " budget at " + formatCurrency(apartment.getRentMonthly()) + " per month");
        reasons.add("it fits your household of " + profile.getFamilySize()
                + " with room for up to " + apartment.getMaxOccupancy());

        String amenityReason = buildAmenityReason(apartment, profile, scoreBreakdown);
        if (amenityReason != null) {
            reasons.add(amenityReason);
        }

        if (Boolean.TRUE.equals(profile.getHasPets()) && Boolean.TRUE.equals(apartment.getPetFriendly())) {
            reasons.add("it is pet-friendly");
        }

        reasons.add("it is available by " + apartment.getAvailableFrom().format(DATE_FORMATTER));

        if (profile.getIncomeMonthly() != null && apartment.getMinIncomeRequired() != null) {
            reasons.add("your stated monthly income meets the " + formatCurrency(apartment.getMinIncomeRequired())
                    + " minimum requirement");
        }

        return "This apartment matches because " + joinReasons(reasons) + ".";
    }

    public String buildItemExplanation(Apartment apartment) {
        List<String> parts = new ArrayList<>();
        parts.add("This listing in " + apartment.getCity() + " accommodates up to " + apartment.getMaxOccupancy()
                + " occupants");
        parts.add("it is available from " + apartment.getAvailableFrom().format(DATE_FORMATTER));
        parts.add(Boolean.TRUE.equals(apartment.getPetFriendly()) ? "it is pet-friendly" : "it does not allow pets");
        parts.add("qualifying requires monthly income of at least "
                + formatCurrency(apartment.getMinIncomeRequired())
                + " for the " + formatCurrency(apartment.getRentMonthly()) + " rent");

        if (apartment.getAmenities() != null && !apartment.getAmenities().isEmpty()) {
            parts.add("it includes amenities such as " + humanizeList(apartment.getAmenities()));
        }

        return capitalize("this listing " + joinReasons(parts) + ".");
    }

    private String buildAmenityReason(
            Apartment apartment,
            RecommendationRequest profile,
            ScoringService.ScoreBreakdown scoreBreakdown) {
        List<String> wantedAmenities = profile.getAmenitiesWanted();
        if (wantedAmenities == null || wantedAmenities.isEmpty()) {
            return null;
        }

        List<String> matchedAmenities = wantedAmenities.stream()
                .filter(wanted -> apartment.getAmenities() != null
                        && apartment.getAmenities().stream().anyMatch(available -> available.equalsIgnoreCase(wanted)))
                .distinct()
                .toList();

        if (matchedAmenities.isEmpty()) {
            return null;
        }

        if (scoreBreakdown.amenityOverlap().compareTo(BigDecimal.ONE) == 0) {
            return "it covers all of the amenities you asked for: " + humanizeList(matchedAmenities);
        }

        return "it covers some of the amenities you wanted, including " + humanizeList(matchedAmenities);
    }

    private String joinReasons(List<String> reasons) {
        if (reasons.size() == 1) {
            return reasons.get(0);
        }

        if (reasons.size() == 2) {
            return reasons.get(0) + " and " + reasons.get(1);
        }

        String allButLast = reasons.subList(0, reasons.size() - 1).stream()
                .collect(Collectors.joining(", "));
        return allButLast + ", and " + reasons.get(reasons.size() - 1);
    }

    private String humanizeList(List<String> values) {
        List<String> cleaned = values.stream()
                .map(String::trim)
                .toList();

        return joinReasons(cleaned);
    }

    private String formatCurrency(BigDecimal amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
