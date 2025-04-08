package com.hubspot.hubspot_integration.dto;

public record ContactRequestDTO(
        String firstName,
        String lastName,
        String email,
        String phone
) {}
