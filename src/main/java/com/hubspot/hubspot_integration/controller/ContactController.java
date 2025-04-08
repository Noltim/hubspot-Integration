package com.hubspot.hubspot_integration.controller;

import com.hubspot.hubspot_integration.dto.ContactRequestDTO;
import com.hubspot.hubspot_integration.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody ContactRequestDTO dto) {
        String result = contactService.createContact(dto);
        return ResponseEntity.ok(result);
    }
}
