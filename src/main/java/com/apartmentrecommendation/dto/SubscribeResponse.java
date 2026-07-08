package com.apartmentrecommendation.dto;

public class SubscribeResponse {

    private String subscriptionId;
    private String message;

    public SubscribeResponse(String subscriptionId, String message) {
        this.subscriptionId = subscriptionId;
        this.message = message;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
