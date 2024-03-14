package com.example.carrentingapp.email.contact_form.request;

import lombok.Data;

@Data
public class AuthorizedContactFormRequest {
    private String subject;
    private String body;
}
