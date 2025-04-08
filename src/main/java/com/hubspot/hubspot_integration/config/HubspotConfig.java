package com.hubspot.hubspot_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hubspot.oauth")
public class HubspotConfig {

    private String appId;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String authUri;
    private String tokenUri;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthUrl() {
        return authUri;
    }

    public void setAuthUrl(String authUri) {
        this.authUri = authUri;
    }

    public String getTokenUrl() {
        return tokenUri;
    }

    public void setTokenUrl(String tokenUri) {
        this.tokenUri = tokenUri;
    }
}
