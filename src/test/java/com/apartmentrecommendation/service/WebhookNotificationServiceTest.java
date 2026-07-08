package com.apartmentrecommendation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;
import org.springframework.test.web.client.MockRestServiceServer;

import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.entity.Subscription;
import com.apartmentrecommendation.repository.SubscriptionRepository;

class WebhookNotificationServiceTest {

    @Test
    void shouldPostNotificationWhenNewApartmentMatchesSubscription() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
        WebhookNotificationService service = service(subscriptionRepository, restClientBuilder);

        when(subscriptionRepository.findAll()).thenReturn(List.of(baseSubscription("Austin", true)));

        server.expect(once(), requestTo("https://example.com/webhook"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        service.notifyMatchingSubscribers(baseApartment("Austin", true));

        server.verify();
    }

    @Test
    void shouldSkipWebhookWhenNewApartmentDoesNotMatchSubscription() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
        WebhookNotificationService service = service(subscriptionRepository, restClientBuilder);

        when(subscriptionRepository.findAll()).thenReturn(List.of(baseSubscription("Austin", true)));

        service.notifyMatchingSubscribers(baseApartment("Seattle", true));

        server.verify();
    }

    @Test
    void shouldNotFailWhenWebhookReturnsError() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
        WebhookNotificationService service = service(subscriptionRepository, restClientBuilder);

        when(subscriptionRepository.findAll()).thenReturn(List.of(baseSubscription("Austin", true)));

        server.expect(once(), requestTo("https://example.com/webhook"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertDoesNotThrow(() -> service.notifyMatchingSubscribers(baseApartment("Austin", true)));
        server.verify();
    }

    private WebhookNotificationService service(
            SubscriptionRepository subscriptionRepository,
            RestClient.Builder restClientBuilder) {
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        EligibilityService eligibilityService = new EligibilityService();
        ScoringService scoringService = new ScoringService();
        ExplanationService explanationService = new ExplanationService();

        return new WebhookNotificationService(
                subscriptionRepository,
                subscriptionService,
                eligibilityService,
                scoringService,
                explanationService,
                restClientBuilder);
    }

    private Apartment baseApartment(String city, boolean petFriendly) {
        Apartment apartment = new Apartment();
        apartment.setId("apt_webhook");
        apartment.setTitle("Webhook Match Apartment");
        apartment.setCity(city);
        apartment.setRentMonthly(new BigDecimal("1700.00"));
        apartment.setMaxOccupancy(4);
        apartment.setAvailableFrom(LocalDate.of(2026, 7, 15));
        apartment.setPetFriendly(petFriendly);
        apartment.setAmenities(List.of("parking", "gym"));
        apartment.setMinIncomeRequired(new BigDecimal("5100.00"));
        apartment.setDescription("Test listing");
        return apartment;
    }

    private Subscription baseSubscription(String city, boolean hasPets) {
        Subscription subscription = new Subscription();
        subscription.setId("sub_test");
        subscription.setWebhookUrl("https://example.com/webhook");
        subscription.setBudgetMax(new BigDecimal("1800.00"));
        subscription.setFamilySize(3);
        subscription.setCity(city);
        subscription.setMoveInDate(LocalDate.of(2026, 8, 1));
        subscription.setHasPets(hasPets);
        subscription.setAmenitiesWanted(List.of("parking", "gym"));
        subscription.setIncomeMonthly(new BigDecimal("6000.00"));
        subscription.setCreatedAt(Instant.now());
        return subscription;
    }
}
