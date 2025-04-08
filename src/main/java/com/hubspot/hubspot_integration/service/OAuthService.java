package com.hubspot.hubspot_integration.service;

import com.hubspot.hubspot_integration.config.HubspotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OAuthService {

    private final HubspotConfig config;

    public OAuthService(HubspotConfig config) {
        this.config = config;
    }

    public String generateAuthorizationUrl() {
        return config.getAuthUrl()
                + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + URLEncoder.encode(config.getRedirectUri(), StandardCharsets.UTF_8)
                + "&scope=" + config.getScope()
                + "&response_type=code";
    }
}
