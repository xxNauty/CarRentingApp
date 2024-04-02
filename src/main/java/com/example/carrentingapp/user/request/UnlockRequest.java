package com.example.carrentingapp.user.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class UnlockRequest implements Request {

    public Optional<String> userid;

    @Override
    public void checkInput() {
        if (this.userid.isPresent()) {
            RequestValidationService.validateToken(this.userid.get());
        } else {
            throw new InvalidArgumentException("You have to pass id of user you want to unlock");
        }
    }
}
