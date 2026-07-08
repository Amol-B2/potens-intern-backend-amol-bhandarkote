package com.apartmentrecommendation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.exception.ResourceNotFoundException;
import com.apartmentrecommendation.repository.ApartmentRepository;

@Service
public class ItemService {

    private final ApartmentRepository apartmentRepository;

    public ItemService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    public Apartment findById(String itemId) {
        return apartmentRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found for id: " + itemId));
    }

    public Apartment create(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public Apartment update(String itemId, Apartment request) {
        Apartment apartment = findById(itemId);
        apartment.setTitle(request.getTitle());
        apartment.setCity(request.getCity());
        apartment.setRentMonthly(request.getRentMonthly());
        apartment.setMaxOccupancy(request.getMaxOccupancy());
        apartment.setAvailableFrom(request.getAvailableFrom());
        apartment.setPetFriendly(request.getPetFriendly());
        apartment.setAmenities(request.getAmenities());
        apartment.setMinIncomeRequired(request.getMinIncomeRequired());
        apartment.setDescription(request.getDescription());
        return apartmentRepository.save(apartment);
    }

    public void delete(String itemId) {
        Apartment apartment = findById(itemId);
        apartmentRepository.delete(apartment);
    }
}
