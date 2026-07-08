package com.apartmentrecommendation.dto;

import java.math.BigDecimal;

public class WebhookNotification {

    private String subscriptionId;
    private String matchedItemId;
    private String title;
    private String city;
    private BigDecimal rentMonthly;
    private BigDecimal matchScore;
    private String reason;

    public WebhookNotification(
            String subscriptionId,
            String matchedItemId,
            String title,
            String city,
            BigDecimal rentMonthly,
            BigDecimal matchScore,
            String reason) {
        this.subscriptionId = subscriptionId;
        this.matchedItemId = matchedItemId;
        this.title = title;
        this.city = city;
        this.rentMonthly = rentMonthly;
        this.matchScore = matchScore;
        this.reason = reason;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getMatchedItemId() {
        return matchedItemId;
    }

    public void setMatchedItemId(String matchedItemId) {
        this.matchedItemId = matchedItemId;
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
