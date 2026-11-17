package com.example.tourist.dto;

public record TokenResponse(String tokenType, String accessToken, long expiresInSeconds) {}
