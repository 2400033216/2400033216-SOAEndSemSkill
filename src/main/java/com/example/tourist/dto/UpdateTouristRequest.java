package com.example.tourist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateTouristRequest(
        @Size(max = 120, message = "Full name must not exceed 120 characters")
        String fullName,

        @Email(message = "Email must be valid")
        @Size(max = 180, message = "Email must not exceed 180 characters")
        String email,

        @Pattern(regexp = "^[+0-9() .-]{7,25}$", message = "Contact number must contain 7-25 valid phone characters")
        String contactNumber,

        @Size(max = 30, message = "A maximum of 30 visited destinations is allowed")
        List<@Size(min = 2, max = 160, message = "Each destination must be 2-160 characters") String> visitedDestinations
) {}
