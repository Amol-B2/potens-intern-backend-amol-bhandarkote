package com.apartmentrecommendation.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.repository.ApartmentRepository;

@Configuration
public class SeedDataLoader {

    @Bean
    CommandLineRunner loadApartments(ApartmentRepository apartmentRepository) {
        return args -> {
            if (apartmentRepository.count() > 0) {
                return;
            }

            apartmentRepository.saveAll(List.of(
                    apartment(
                            "apt_001",
                            "Downtown Smart Studio",
                            "Austin",
                            "1450.00",
                            2,
                            LocalDate.of(2026, 7, 10),
                            true,
                            List.of("parking", "gym", "laundry"),
                            "4350.00",
                            "Compact studio in central Austin with gym access, secure parking, and shared laundry."),
                    apartment(
                            "apt_002",
                            "Riverside Family Flat",
                            "Austin",
                            "1700.00",
                            4,
                            LocalDate.of(2026, 7, 15),
                            true,
                            List.of("parking", "gym", "laundry"),
                            "5100.00",
                            "Bright two-bedroom flat near the riverside trail with parking, gym access, and in-building laundry."),
                    apartment(
                            "apt_003",
                            "Capitol View Loft",
                            "Austin",
                            "2100.00",
                            3,
                            LocalDate.of(2026, 8, 5),
                            false,
                            List.of("gym", "pool", "laundry"),
                            "6300.00",
                            "Modern loft with skyline views, upgraded finishes, and easy access to downtown employers."),
                    apartment(
                            "apt_004",
                            "North Austin Budget One-Bed",
                            "Austin",
                            "1250.00",
                            2,
                            LocalDate.of(2026, 7, 1),
                            false,
                            List.of("laundry", "parking"),
                            "3750.00",
                            "Affordable one-bedroom option in North Austin with practical amenities and easy highway access."),
                    apartment(
                            "apt_005",
                            "Ballard Marina Apartment",
                            "Seattle",
                            "1850.00",
                            2,
                            LocalDate.of(2026, 7, 20),
                            true,
                            List.of("parking", "laundry", "pet-wash"),
                            "5550.00",
                            "Comfortable apartment near Ballard marina with pet-friendly extras and reserved parking."),
                    apartment(
                            "apt_006",
                            "Capitol Hill Micro Loft",
                            "Seattle",
                            "1600.00",
                            1,
                            LocalDate.of(2026, 8, 1),
                            true,
                            List.of("gym", "laundry"),
                            "4800.00",
                            "Efficient loft for solo renters who want a walkable neighborhood and a smaller footprint."),
                    apartment(
                            "apt_007",
                            "Green Lake Family Home",
                            "Seattle",
                            "2500.00",
                            5,
                            LocalDate.of(2026, 7, 25),
                            false,
                            List.of("parking", "laundry", "storage"),
                            "7500.00",
                            "Spacious family-focused home near Green Lake with extra storage and driveway parking."),
                    apartment(
                            "apt_008",
                            "Fremont Work-From-Home Suite",
                            "Seattle",
                            "2200.00",
                            3,
                            LocalDate.of(2026, 8, 10),
                            true,
                            List.of("parking", "gym", "workspace"),
                            "6600.00",
                            "Well-lit Fremont unit with a dedicated workspace and balanced amenities for hybrid workers."),
                    apartment(
                            "apt_009",
                            "LoDo City Apartment",
                            "Denver",
                            "1750.00",
                            2,
                            LocalDate.of(2026, 7, 12),
                            true,
                            List.of("gym", "parking", "laundry"),
                            "5250.00",
                            "Central Denver apartment near LoDo transit with parking, laundry, and a resident fitness room."),
                    apartment(
                            "apt_010",
                            "Cherry Creek Comfort Condo",
                            "Denver",
                            "2300.00",
                            4,
                            LocalDate.of(2026, 8, 3),
                            false,
                            List.of("parking", "pool", "storage"),
                            "6900.00",
                            "Upscale condo in Cherry Creek with roomy living areas, storage, and a quiet residential feel."),
                    apartment(
                            "apt_011",
                            "Highlands Starter Flat",
                            "Denver",
                            "1500.00",
                            2,
                            LocalDate.of(2026, 7, 5),
                            true,
                            List.of("laundry", "parking"),
                            "4500.00",
                            "Starter-friendly flat in the Highlands with simple amenities and a commuter-friendly location."),
                    apartment(
                            "apt_012",
                            "Tech Corridor Two-Bed",
                            "Denver",
                            "1950.00",
                            3,
                            LocalDate.of(2026, 7, 28),
                            true,
                            List.of("parking", "gym", "laundry", "workspace"),
                            "5850.00",
                            "Two-bedroom option designed for shared living with good amenities and flexible work space."),
                    apartment(
                            "apt_013",
                            "Lakeview Corner Apartment",
                            "Chicago",
                            "1650.00",
                            2,
                            LocalDate.of(2026, 7, 18),
                            true,
                            List.of("laundry", "gym"),
                            "4950.00",
                            "Lakeview corner unit with natural light, on-site laundry, and a compact resident gym."),
                    apartment(
                            "apt_014",
                            "West Loop Shared Living Loft",
                            "Chicago",
                            "2050.00",
                            4,
                            LocalDate.of(2026, 8, 1),
                            false,
                            List.of("parking", "gym", "laundry"),
                            "6150.00",
                            "Open-plan loft in West Loop suited to roommates who want transit access and modern finishes."),
                    apartment(
                            "apt_015",
                            "Hyde Park Budget Studio",
                            "Chicago",
                            "1200.00",
                            1,
                            LocalDate.of(2026, 7, 8),
                            true,
                            List.of("laundry"),
                            "3600.00",
                            "Budget-conscious studio in Hyde Park with straightforward amenities and strong value."),
                    apartment(
                            "apt_016",
                            "Lincoln Park Family Rental",
                            "Chicago",
                            "2400.00",
                            5,
                            LocalDate.of(2026, 7, 30),
                            true,
                            List.of("parking", "laundry", "storage", "play-area"),
                            "7200.00",
                            "Large family rental in Lincoln Park with extra storage, parking, and space for daily routines.")));
        };
    }

    private Apartment apartment(
            String id,
            String title,
            String city,
            String rentMonthly,
            Integer maxOccupancy,
            LocalDate availableFrom,
            Boolean petFriendly,
            List<String> amenities,
            String minIncomeRequired,
            String description) {
        Apartment apartment = new Apartment();
        apartment.setId(id);
        apartment.setTitle(title);
        apartment.setCity(city);
        apartment.setRentMonthly(new BigDecimal(rentMonthly));
        apartment.setMaxOccupancy(maxOccupancy);
        apartment.setAvailableFrom(availableFrom);
        apartment.setPetFriendly(petFriendly);
        apartment.setAmenities(amenities);
        apartment.setMinIncomeRequired(new BigDecimal(minIncomeRequired));
        apartment.setDescription(description);
        return apartment;
    }
}
