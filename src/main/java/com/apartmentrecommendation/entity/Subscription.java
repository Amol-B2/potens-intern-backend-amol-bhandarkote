package com.apartmentrecommendation.entity;

import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "webhook_url", nullable = false, length = 500)
    private String webhookUrl;

    @Column(name = "budget_max", nullable = false, precision = 10, scale = 2)
    private BigDecimal budgetMax;

    @Column(name = "family_size", nullable = false)
    private Integer familySize;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(name = "move_in_date", nullable = false)
    private LocalDate moveInDate;

    @Column(name = "has_pets", nullable = false)
    private Boolean hasPets;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_amenities", joinColumns = @JoinColumn(name = "subscription_id"))
    @Column(name = "amenity", nullable = false, length = 80)
    private List<String> amenitiesWanted = new ArrayList<>();

    @Column(name = "income_monthly", precision = 10, scale = 2)
    private BigDecimal incomeMonthly;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
