package com.hubspot.hubspot_integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.hubspot_integration.config.HubspotConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private final HubspotConfig config;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    private volatile String accessToken;
    private volatile String refreshToken;
    private volatile Instant expiresAt;

    public TokenService(HubspotConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    public String exchangeCodeForToken(String code) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", config.getClientId());
            params.put("client_secret", config.getClientSecret());
            params.put("redirect_uri", config.getRedirectUri());
            params.put("code", code);

            String form = params.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getTokenUrl()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode jsonNode = objectMapper.readTree(response.body());

            accessToken = jsonNode.get("access_token").asText();
            refreshToken = jsonNode.get("refresh_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();
            expiresAt = Instant.now().plusSeconds(expiresIn);

            return accessToken;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao trocar o código por token");
        }
    }

    public String getAccessToken() {
        if (isTokenExpired()) {
            throw new IllegalStateException("Token expirado. Refaça a autenticação.");
        }
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isTokenExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

    public void clearTokens() {
        this.accessToken = null;
        this.refreshToken = null;
        this.expiresAt = null;
    }
}
