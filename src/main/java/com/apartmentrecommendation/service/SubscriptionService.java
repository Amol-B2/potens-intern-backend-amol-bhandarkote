package com.apartmentrecommendation.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.dto.SubscribeRequest;
import com.apartmentrecommendation.dto.SubscribeResponse;
import com.apartmentrecommendation.entity.Subscription;
import com.apartmentrecommendation.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscribeResponse subscribe(SubscribeRequest request) {
        Subscription subscription = toSubscription(request);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return new SubscribeResponse(savedSubscription.getId(), "Subscription created");
    }

    public RecommendationRequest toRecommendationRequest(Subscription subscription) {
        RecommendationRequest profile = new RecommendationRequest();
        profile.setBudgetMax(subscription.getBudgetMax());
        profile.setFamilySize(subscription.getFamilySize());
        profile.setCity(subscription.getCity());
        profile.setMoveInDate(subscription.getMoveInDate());
        profile.setHasPets(subscription.getHasPets());
        profile.setAmenitiesWanted(subscription.getAmenitiesWanted());
        profile.setIncomeMonthly(subscription.getIncomeMonthly());
        return profile;
    }

    private Subscription toSubscription(SubscribeRequest request) {
        RecommendationRequest profile = request.getProfile();

        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID().toString());
        subscription.setWebhookUrl(request.getWebhookUrl());
        subscription.setBudgetMax(profile.getBudgetMax());
        subscription.setFamilySize(profile.getFamilySize());
        subscription.setCity(profile.getCity());
        subscription.setMoveInDate(profile.getMoveInDate());
        subscription.setHasPets(profile.getHasPets());
        subscription.setAmenitiesWanted(profile.getAmenitiesWanted() == null ? List.of() : profile.getAmenitiesWanted());
        subscription.setIncomeMonthly(profile.getIncomeMonthly());
        subscription.setCreatedAt(Instant.now());
        return subscription;
    }
}
