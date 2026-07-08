package com.apartmentrecommendation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApartmentMatchResponse {

    private String id;
    private String title;
    private String city;
    private BigDecimal rentMonthly;
    private Integer maxOccupancy;
    private LocalDate availableFrom;
    private Boolean petFriendly;
    private List<String> amenities = new ArrayList<>();
    private BigDecimal minIncomeRequired;
    private String description;
    private BigDecimal matchScore;
    private String reason;

    public ApartmentMatchResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getRentMonthly() {
        return rentMonthly;
    }

    public void setRentMonthly(BigDecimal rentMonthly) {
        this.rentMonthly = rentMonthly;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Boolean getPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(Boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public BigDecimal getMinIncomeRequired() {
        return minIncomeRequired;
    }

    public void setMinIncomeRequired(BigDecimal minIncomeRequired) {
        this.minIncomeRequired = minIncomeRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
