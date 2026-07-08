package com.apartmentrecommendation.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(name = "rent_monthly", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentMonthly;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "available_from", nullable = false)
    private LocalDate availableFrom;

    @Column(name = "pet_friendly", nullable = false)
    private Boolean petFriendly;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "apartment_amenities", joinColumns = @JoinColumn(name = "apartment_id"))
    @Column(name = "amenity", nullable = false, length = 80)
    private List<String> amenities = new ArrayList<>();

    @Column(name = "min_income_required", nullable = false, precision = 10, scale = 2)
    private BigDecimal minIncomeRequired;

    @Column(nullable = false, length = 500)
    private String description;

    public Apartment() {
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
}
