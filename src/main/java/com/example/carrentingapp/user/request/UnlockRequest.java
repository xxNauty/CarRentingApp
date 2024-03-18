package com.example.carrentingapp.user.request;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UnlockRequest {
    private UUID userid;
}
