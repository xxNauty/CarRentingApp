package com.example.carrentingapp.email.notifications.confirm_email.token;

import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserBaseRepository repository;

    public String createToken(UserBase user){
        ConfirmationToken token = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        repository.save(user);
        confirmationTokenRepository.save(token);

        return token.getToken();
    }
}
