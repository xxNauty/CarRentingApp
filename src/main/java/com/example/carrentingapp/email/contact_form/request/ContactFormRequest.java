package com.example.carrentingapp.email.contact_form.request;

import lombok.Data;

@Data
public class ContactFormRequest {
    private String email;
    private String subject;
    private String body;
}
