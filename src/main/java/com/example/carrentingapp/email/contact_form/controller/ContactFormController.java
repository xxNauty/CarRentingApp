package com.example.carrentingapp.email.contact_form.controller;

import com.example.carrentingapp.email.contact_form.request.AuthorizedContactFormRequest;
import com.example.carrentingapp.email.contact_form.request.ContactFormRequest;
import com.example.carrentingapp.email.contact_form.response.ContactFormResponse;
import com.example.carrentingapp.email.contact_form.service.ContactFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact_form")
public class ContactFormController {

    private final ContactFormService service;

    @PostMapping("/no_auth")
    public ResponseEntity<ContactFormResponse> sendMessage(
            @RequestBody ContactFormRequest request
    ){
        return ResponseEntity.ok(service.sendMessageToAdminAsNotAuthorized(request));
    }

    @PostMapping("/auth")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ContactFormResponse> sendMessage(
            @RequestBody AuthorizedContactFormRequest request
    ){
        return ResponseEntity.ok(service.sendMessageToAdminAsAuthorized(request));
    }

}
