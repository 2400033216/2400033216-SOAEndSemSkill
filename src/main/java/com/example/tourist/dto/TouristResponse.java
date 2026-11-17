package com.example.tourist.dto;

import com.example.tourist.entity.Tourist;
import java.time.Instant;
import java.util.List;

public record TouristResponse(
        Long id,
        String fullName,
        String email,
        String contactNumber,
        List<String> visitedDestinations,
        Instant createdAt,
        Instant updatedAt
) {
    public static TouristResponse from(Tourist tourist) {
        return new TouristResponse(
                tourist.getId(), tourist.getFullName(), tourist.getEmail(),
                tourist.getContactNumber(), List.copyOf(tourist.getVisitedDestinations()),
                tourist.getCreatedAt(), tourist.getUpdatedAt()
        );
    }
}
