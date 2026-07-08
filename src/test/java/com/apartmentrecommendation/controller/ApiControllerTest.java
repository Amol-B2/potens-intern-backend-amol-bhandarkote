package com.apartmentrecommendation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.entity.Subscription;
import com.apartmentrecommendation.repository.ApartmentRepository;
import com.apartmentrecommendation.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "app.admin.token=test-admin-token")
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApartmentRepository apartmentRepository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(apartmentRepository);
        Mockito.reset(subscriptionRepository);
        when(subscriptionRepository.findAll()).thenReturn(List.of());
    }

    @Test
    void recommendShouldReturn400WhenRequiredFieldIsMissing() throws Exception {
        String requestBody = """
                {
                  "budgetMax": 1800,
                  "familySize": 3,
                  "moveInDate": "2026-08-01",
                  "hasPets": true
                }
                """;

        mockMvc.perform(post("/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.city").exists());
    }

    @Test
    void recommendShouldReturnEmptyMatchesWhenNoApartmentIsEligible() throws Exception {
        when(apartmentRepository.findAll()).thenReturn(List.of(baseApartment("apt_001", "Seattle", false)));

        String requestBody = """
                {
                  "budgetMax": 1800,
                  "familySize": 3,
                  "city": "Austin",
                  "moveInDate": "2026-08-01",
                  "hasPets": true,
                  "amenitiesWanted": ["parking", "gym"],
                  "incomeMonthly": 6000
                }
                """;

        mockMvc.perform(post("/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches").isArray())
                .andExpect(jsonPath("$.matches").isEmpty());
    }

    @Test
    void explainShouldReturn404WhenItemIsMissing() throws Exception {
        when(apartmentRepository.findById("missing-id")).thenReturn(Optional.empty());

        mockMvc.perform(get("/explain/missing-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Apartment not found for id: missing-id"));
    }

    @Test
    void itemsShouldReturn401WhenAdminTokenIsMissing() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void subscribeShouldCreateProfileWebhookSubscription() throws Exception {
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String requestBody = """
                {
                  "webhookUrl": "https://example.com/webhook",
                  "profile": {
                    "budgetMax": 1800,
                    "familySize": 3,
                    "city": "Austin",
                    "moveInDate": "2026-08-01",
                    "hasPets": true,
                    "amenitiesWanted": ["parking", "gym"],
                    "incomeMonthly": 6000
                  }
                }
                """;

        mockMvc.perform(post("/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subscriptionId").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Subscription created"));
    }

    @Test
    void itemsCrudShouldWorkWhenAdminTokenIsPresent() throws Exception {
        Apartment apartment = baseApartment("apt_200", "Austin", true);

        when(apartmentRepository.findAll()).thenReturn(List.of(apartment));
        when(apartmentRepository.findById("apt_200")).thenReturn(Optional.of(apartment));
        when(apartmentRepository.save(any(Apartment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(apartmentRepository).delete(any(Apartment.class));

        mockMvc.perform(get("/items").header("x-admin-token", "test-admin-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("apt_200"));

        mockMvc.perform(post("/items")
                        .header("x-admin-token", "test-admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apartment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("apt_200"));

        mockMvc.perform(put("/items/apt_200")
                        .header("x-admin-token", "test-admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("apt_200"));

        mockMvc.perform(delete("/items/apt_200").header("x-admin-token", "test-admin-token"))
                .andExpect(status().isNoContent());
    }

    private Apartment baseApartment(String id, String city, boolean petFriendly) {
        Apartment apartment = new Apartment();
        apartment.setId(id);
        apartment.setTitle("Test Apartment");
        apartment.setCity(city);
        apartment.setRentMonthly(new BigDecimal("1700.00"));
        apartment.setMaxOccupancy(4);
        apartment.setAvailableFrom(LocalDate.of(2026, 7, 15));
        apartment.setPetFriendly(petFriendly);
        apartment.setAmenities(List.of("parking", "gym", "laundry"));
        apartment.setMinIncomeRequired(new BigDecimal("5100.00"));
        apartment.setDescription("Test listing for controller tests");
        return apartment;
    }
}
