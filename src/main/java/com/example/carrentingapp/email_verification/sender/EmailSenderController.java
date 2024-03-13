package com.example.carrentingapp.email_verification.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSender sender;

    //TODO: DO USUNIĘCIA PO SKOŃCZENIU KONFIGUROWANIA WYSYŁANIA MAILI
    @GetMapping("/sendTest")
    public void sendEmail(){
        for (int i = 0; i < 10; i++) {
            sender.send(
                    "test@test.pl",
                    "subject",
                    "<h1> Test email "+i+"</h1>"
            );
        }
    }
}
