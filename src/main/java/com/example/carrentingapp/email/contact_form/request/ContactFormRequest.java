package com.example.carrentingapp.email.contact_form.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactFormRequest {
    private String email;
    private String subject;
    private String body;
}