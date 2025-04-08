package com.hubspot.hubspot_integration.controller;

import com.hubspot.hubspot_integration.service.OAuthService;
import com.hubspot.hubspot_integration.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private TokenService tokenService;


    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUrl() {
        String authUrl = oAuthService.generateAuthorizationUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        String accessToken = tokenService.exchangeCodeForToken(code);
        return ResponseEntity.ok("Token recebido com sucesso: " + accessToken);
    }

}
