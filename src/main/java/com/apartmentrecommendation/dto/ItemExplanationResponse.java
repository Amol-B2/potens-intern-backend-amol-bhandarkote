package com.apartmentrecommendation.dto;

public class ItemExplanationResponse {

    private String itemId;
    private String explanation;

    public ItemExplanationResponse() {
    }

    public ItemExplanationResponse(String itemId, String explanation) {
        this.itemId = itemId;
        this.explanation = explanation;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
