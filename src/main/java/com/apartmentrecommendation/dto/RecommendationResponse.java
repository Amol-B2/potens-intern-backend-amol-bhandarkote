package com.apartmentrecommendation.dto;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResponse {

    private List<ApartmentMatchResponse> matches = new ArrayList<>();

    public RecommendationResponse() {
    }

    public RecommendationResponse(List<ApartmentMatchResponse> matches) {
        this.matches = matches;
    }

    public List<ApartmentMatchResponse> getMatches() {
        return matches;
    }

    public void setMatches(List<ApartmentMatchResponse> matches) {
        this.matches = matches;
    }
}
