package com.example.carrentingapp.email.contact_form.service;

import com.example.carrentingapp.email.contact_form.ContactFormMessage;
import com.example.carrentingapp.email.contact_form.ContactFormMessageRepository;
import com.example.carrentingapp.email.contact_form.request.AuthorizedContactFormRequest;
import com.example.carrentingapp.email.contact_form.request.ContactFormRequest;
import com.example.carrentingapp.email.contact_form.response.ContactFormResponse;
import com.example.carrentingapp.email.sender.EmailSender;
import com.example.carrentingapp.email.sender.EmailService;
import com.example.carrentingapp.user.BaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContactFormService {

    private final ContactFormMessageRepository contactFormMessageRepository;
    private final EmailSender service;

    public ContactFormResponse sendMessageToAdminAsNotAuthorized(ContactFormRequest request){
        ContactFormMessage message = new ContactFormMessage(
                request.getEmail(),
                "adam@kowalski.pl",
                request.getSubject(),
                request.getBody(),
                LocalDateTime.now()
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        ContactFormMessage message = new ContactFormMessage(
                ((BaseUser) authentication.getPrincipal()).getEmail(),
                "adam@kowalski.pl",
                request.getSubject(),
                request.getBody(),
                LocalDateTime.now()
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
