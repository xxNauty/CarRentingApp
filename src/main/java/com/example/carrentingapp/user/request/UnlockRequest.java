package com.example.carrentingapp.user.request;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnlockRequest {
    private UUID userid;
}
