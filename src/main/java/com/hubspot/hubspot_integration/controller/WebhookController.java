package com.hubspot.hubspot_integration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody Object payload) {
        logger.info("🔔 Webhook recebido: {}", payload);
        return ResponseEntity.ok("Webhook recebido com sucesso!");
    }

    @GetMapping
    public ResponseEntity<String> webhookHealthCheck() {
        return ResponseEntity.ok("Webhook ativo!");
    }
}
