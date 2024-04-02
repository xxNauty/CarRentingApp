package com.example.carrentingapp.email.contact_form.service;

import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.email.contact_form.request.AuthorizedContactFormRequest;
import com.example.carrentingapp.email.contact_form.request.ContactFormRequest;
import com.example.carrentingapp.email.contact_form.response.ContactFormResponse;
import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactFormService {

    private final EmailSender service;
    private final SecurityService securityService;

    public ContactFormResponse sendMessageToAdminAsNotAuthorized(ContactFormRequest request) {
        request.checkInput();

        service.send(
                "adam@kowalski.pl",
                request.email.get(),
                request.subject.get(),
                request.body.get(),
                EmailMessage.EmailMessageType.CONTACT_FORM
        );
        return new ContactFormResponse("Message sent successfully");
    }

    public ContactFormResponse sendMessageToAdminAsAuthorized(AuthorizedContactFormRequest request) {
        request.checkInput();

        service.send(
                "adam@kowalski.pl",
                securityService.getLoggedInUser().getEmail(),
                request.subject.get(),
                request.body.get(),
                EmailMessage.EmailMessageType.CONTACT_FORM
        );
        return new ContactFormResponse("Message sent successfully");
    }
}
