package com.apartmentrecommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apartmentrecommendation.entity.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, String> {

}