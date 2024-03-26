package com.example.carrentingapp.authentication.service;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.request.SendVerifyingTokenAgainRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.EmailVerificationResponse;
import com.example.carrentingapp.configuration.jwt.JwtService;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailRequest;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationToken;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenRepository;
import com.example.carrentingapp.exception.exception.http_error_500.AccountAlreadyEnabledException;
import com.example.carrentingapp.exception.exception.http_error_500.EmailAlreadyVerifiedException;
import com.example.carrentingapp.exception.exception.http_error_403.TokenExpiredException;
import com.example.carrentingapp.exception.exception.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.token.Token;
import com.example.carrentingapp.token.TokenRepository;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.service.UserCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService {

    private final UserBaseRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserCreateService userCreateService;
    private final NotificationSender notificationSender;

    public AuthenticationResponse register(RegistrationRequest request) {
        request.checkInput();
        UserBase user = userCreateService.createUser(
                request.firstName.get(),
                request.lastName.get(),
                request.email.get(),
                request.password.get(),
                request.dateOfBirth.get()
        );
        UserBase savedUser = repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(
                jwtToken,
                refreshToken
        );
    }

    public AuthenticationResponse login(LoginRequest request){
        request.checkInput();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email.get(),
                        request.password.get()
                )
        );
        UserBase user = repository.findByEmail(request.email.get())
                .orElseThrow(() -> new UserNotFoundException("There is no user with given Id"));
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(
                jwtToken,
                refreshToken
        );
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            UserBase user = this.repository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("There is no user with given Id"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = new AuthenticationResponse(
                        accessToken,
                        refreshToken
                );
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public EmailVerificationResponse verifyEmail(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("There is no token for this user"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new EmailAlreadyVerifiedException("You already verified your email");
        }

        if(confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Given token already expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.setStatus(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_CONFIRMED);

        UserBase user = confirmationToken.getUser();
        user.setStatus(UserBase.UserStatus.USER_READY);

        repository.save(user);
        confirmationTokenRepository.save(confirmationToken);

        return new EmailVerificationResponse("Email verified, your account is now enabled");
    }

    public EmailVerificationResponse sendVerifyingTokenAgain(SendVerifyingTokenAgainRequest request){
        request.checkInput();
        UserBase user = repository.findByEmailAndId(
                request.email.get(),
                UUID.fromString(request.userId.get())
        ).orElseThrow(() -> new UserNotFoundException("There is no such user"));
        List<ConfirmationToken> oldTokens = confirmationTokenRepository.findAllUnusedForUser(UUID.fromString(request.userId.get()));
        for(ConfirmationToken token : oldTokens){
            token.setStatus(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_EXPIRED);
            confirmationTokenRepository.save(token);
        }
        if (user.isEnabled()){
            throw new AccountAlreadyEnabledException("Your account is already enabled");
        }
        notificationSender.sendConfirmEmailNotification(new ConfirmEmailRequest(user));
        return new EmailVerificationResponse("Email verification token sent again");
    }

    private void saveUserToken(UserBase user, String jwtToken) {
        Token token = new Token(jwtToken, user);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserBase user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId(), Token.JwtTokenStatus.JWT_TOKEN_ACTIVE);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setStatus(Token.JwtTokenStatus.JWT_TOKEN_EXPIRED);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
