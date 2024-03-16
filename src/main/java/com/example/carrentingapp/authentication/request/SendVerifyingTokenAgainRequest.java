package com.example.carrentingapp.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendVerifyingTokenAgainRequest {
    private String email;
    private String userId;
}
