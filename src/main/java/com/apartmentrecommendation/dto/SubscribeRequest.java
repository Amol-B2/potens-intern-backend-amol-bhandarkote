package com.apartmentrecommendation.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {

    @NotBlank(message = "webhookUrl is required")
    @URL(message = "webhookUrl must be a valid URL")
    private String webhookUrl;

    @Valid
    @NotNull(message = "profile is required")
    private RecommendationRequest profile;

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public RecommendationRequest getProfile() {
        return profile;
    }

    public void setProfile(RecommendationRequest profile) {
        this.profile = profile;
    }
}
