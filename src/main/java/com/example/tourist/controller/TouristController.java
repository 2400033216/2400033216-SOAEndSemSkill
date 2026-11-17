package com.example.tourist.controller;

import com.example.tourist.dto.TouristResponse;
import com.example.tourist.dto.UpdateTouristRequest;
import com.example.tourist.service.TouristService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tourists")
@SecurityRequirement(name = "bearerAuth")
public class TouristController {
    private final TouristService touristService;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tourist record")
    public TouristResponse getTourist(@PathVariable Long id) {
        return touristService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update tourist contact details and/or visited destinations",
            description = "Send only fields to change. A valid JWT Bearer token is required.")
    public TouristResponse updateTourist(@PathVariable Long id,
                                         @Valid @RequestBody UpdateTouristRequest request) {
        return touristService.update(id, request);
    }
}
