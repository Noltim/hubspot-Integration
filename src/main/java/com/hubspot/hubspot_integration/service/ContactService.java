package com.hubspot.hubspot_integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.hubspot_integration.dto.ContactRequestDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ContactService {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ContactService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String createContact(ContactRequestDTO dto) {
        try {
            String accessToken = tokenService.getAccessToken();

            Map<String, Object> properties = new HashMap<>();
            properties.put("email", dto.email());
            properties.put("firstname", dto.firstName());
            properties.put("lastname", dto.lastName());
            properties.put("phone", dto.phone());

            Map<String, Object> body = Map.of("properties", properties);

            String json = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.hubapi.com/crm/v3/objects/contacts"))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return "Contato criado com sucesso!";
            } else {
                return "Erro ao criar contato: " + response.body();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Erro interno ao tentar criar contato.";
        }
    }
}
