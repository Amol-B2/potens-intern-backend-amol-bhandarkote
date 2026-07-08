package com.apartmentrecommendation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.apartmentrecommendation.dto.RecommendationRequest;
import com.apartmentrecommendation.dto.WebhookNotification;
import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.entity.Subscription;
import com.apartmentrecommendation.repository.SubscriptionRepository;

@Service
public class WebhookNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookNotificationService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final EligibilityService eligibilityService;
    private final ScoringService scoringService;
    private final ExplanationService explanationService;
    private final RestClient restClient;

    public WebhookNotificationService(
            SubscriptionRepository subscriptionRepository,
            SubscriptionService subscriptionService,
            EligibilityService eligibilityService,
            ScoringService scoringService,
            ExplanationService explanationService,
            RestClient.Builder restClientBuilder) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionService = subscriptionService;
        this.eligibilityService = eligibilityService;
        this.scoringService = scoringService;
        this.explanationService = explanationService;
        this.restClient = restClientBuilder.build();
    }

    public void notifyMatchingSubscribers(Apartment apartment) {
        for (Subscription subscription : subscriptionRepository.findAll()) {
            RecommendationRequest profile = subscriptionService.toRecommendationRequest(subscription);
            if (eligibilityService.isEligible(apartment, profile)) {
                sendNotification(subscription, apartment, profile);
            }
        }
    }

    private void sendNotification(Subscription subscription, Apartment apartment, RecommendationRequest profile) {
        ScoringService.ScoreBreakdown scoreBreakdown = scoringService.calculateScore(apartment, profile);
        WebhookNotification notification = new WebhookNotification(
                subscription.getId(),
                apartment.getId(),
                apartment.getTitle(),
                apartment.getCity(),
                apartment.getRentMonthly(),
                scoreBreakdown.totalScore(),
                explanationService.buildMatchReason(apartment, profile, scoreBreakdown));

        try {
            restClient.post()
                    .uri(subscription.getWebhookUrl())
                    .body(notification)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException exception) {
            LOGGER.warn("Webhook notification failed for subscription {}", subscription.getId(), exception);
        }
    }
}
