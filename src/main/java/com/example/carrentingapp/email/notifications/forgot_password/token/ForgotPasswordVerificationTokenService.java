package com.example.carrentingapp.email.notifications.forgot_password.token;

import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ForgotPasswordVerificationTokenService {

    private final ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;

    public String createToken(BaseUser user){
        ForgotPasswordVerificationToken token = new ForgotPasswordVerificationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        forgotPasswordVerificationTokenRepository.save(token);

        return token.getToken();
    }

}
