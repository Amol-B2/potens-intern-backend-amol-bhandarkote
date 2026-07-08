package com.apartmentrecommendation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecommendationRequest {

    @NotNull(message = "budgetMax is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "budgetMax must be greater than 0")
    private BigDecimal budgetMax;

    @NotNull(message = "familySize is required")
    @Min(value = 1, message = "familySize must be at least 1")
    private Integer familySize;

    @NotBlank(message = "city is required")
    private String city;

    @NotNull(message = "moveInDate is required")
    private LocalDate moveInDate;

    @NotNull(message = "hasPets is required")
    private Boolean hasPets;

    private List<String> amenitiesWanted = new ArrayList<>();

    @DecimalMin(value = "0.0", inclusive = false, message = "incomeMonthly must be greater than 0")
    private BigDecimal incomeMonthly;

    public RecommendationRequest() {
    }

    public BigDecimal getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(BigDecimal budgetMax) {
        this.budgetMax = budgetMax;
    }

    public Integer getFamilySize() {
        return familySize;
    }

    public void setFamilySize(Integer familySize) {
        this.familySize = familySize;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public Boolean getHasPets() {
        return hasPets;
    }

    public void setHasPets(Boolean hasPets) {
        this.hasPets = hasPets;
    }

    public List<String> getAmenitiesWanted() {
        return amenitiesWanted;
    }

    public void setAmenitiesWanted(List<String> amenitiesWanted) {
        this.amenitiesWanted = amenitiesWanted;
    }

    public BigDecimal getIncomeMonthly() {
        return incomeMonthly;
    }

    public void setIncomeMonthly(BigDecimal incomeMonthly) {
        this.incomeMonthly = incomeMonthly;
    }
}
