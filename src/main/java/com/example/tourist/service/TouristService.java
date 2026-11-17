package com.example.tourist.service;

import com.example.tourist.dto.TouristResponse;
import com.example.tourist.dto.UpdateTouristRequest;
import com.example.tourist.entity.Tourist;
import com.example.tourist.repository.TouristRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TouristService {
    private final TouristRepository repository;

    public TouristService(TouristRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public TouristResponse getById(Long id) {
        return TouristResponse.from(findTourist(id));
    }

    @Transactional
    public TouristResponse update(Long id, UpdateTouristRequest request) {
        Tourist tourist = findTourist(id);

        if (request.fullName() != null) {
            if (request.fullName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name cannot be blank");
            }
            tourist.setFullName(request.fullName().trim());
        }
        if (request.email() != null) {
            if (request.email().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be blank");
            }
            String email = request.email().trim();
            if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already used by another tourist");
            }
            tourist.setEmail(email);
        }
        if (request.contactNumber() != null) {
            tourist.setContactNumber(request.contactNumber().trim());
        }
        if (request.visitedDestinations() != null) {
            tourist.setVisitedDestinations(request.visitedDestinations().stream()
                    .map(String::trim).distinct().toList());
        }
        return TouristResponse.from(repository.save(tourist));
    }

    private Tourist findTourist(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tourist record not found for id: " + id));
    }
}
