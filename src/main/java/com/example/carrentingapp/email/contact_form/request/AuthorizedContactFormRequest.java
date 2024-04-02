package com.example.carrentingapp.email.contact_form.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class AuthorizedContactFormRequest implements Request {
    public Optional<String> subject;
    public Optional<String> body;

    @Override
    public void checkInput() {
        if (this.subject.isPresent() && this.body.isPresent()) {
            RequestValidationService.validateParameter(this.subject.get());
            RequestValidationService.validateParameter(this.body.get());
        } else {
            throw new InvalidArgumentException("You have to pass both subject and body in order to sent a message to us");
        }
    }
}
