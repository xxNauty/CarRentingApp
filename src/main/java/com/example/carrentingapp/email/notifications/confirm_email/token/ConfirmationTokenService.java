package com.example.carrentingapp.email.notifications.confirm_email.token;

import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BaseUserRepository repository;

    public String createToken(BaseUser user){
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
