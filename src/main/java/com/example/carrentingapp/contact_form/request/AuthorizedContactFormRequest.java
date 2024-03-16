package com.example.carrentingapp.contact_form.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizedContactFormRequest {
    private String subject;
    private String body;
}
