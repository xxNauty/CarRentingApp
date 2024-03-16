package com.example.carrentingapp.contact_form.service;

import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.contact_form.response.ContactFormResponse;
import com.example.carrentingapp.contact_form.ContactFormMessage;
import com.example.carrentingapp.contact_form.ContactFormMessageRepository;
import com.example.carrentingapp.contact_form.request.AuthorizedContactFormRequest;
import com.example.carrentingapp.contact_form.request.ContactFormRequest;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactFormService {

    private final ContactFormMessageRepository contactFormMessageRepository;
    private final EmailSender service;
    private final SecurityService securityService;

    public ContactFormResponse sendMessageToAdminAsNotAuthorized(ContactFormRequest request){
        ContactFormMessage message = new ContactFormMessage(
                request.getEmail(),
                "adam@kowalski.pl",
                request.getSubject(),
                request.getBody()
        );

        contactFormMessageRepository.save(message);

        service.send(
                message.getRecipientEmail(),
                message.getSenderEmail(),
                message.getSubject(),
                message.getBody()
        );
        return new ContactFormResponse("Message sent successfully");
    }

    public ContactFormResponse sendMessageToAdminAsAuthorized(AuthorizedContactFormRequest request){

        ContactFormMessage message = new ContactFormMessage(
                securityService.getLoggedInUser().getEmail(),
                "adam@kowalski.pl",
                request.getSubject(),
                request.getBody()
        );

        contactFormMessageRepository.save(message);

        service.send(
                message.getRecipientEmail(),
                message.getSenderEmail(),
                message.getSubject(),
                message.getBody()
        );
        return new ContactFormResponse("Message sent successfully");
    }
}