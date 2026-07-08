package com.apartmentrecommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apartmentrecommendation.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
}
