package com.example.carrentingapp.user.request;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LockRequest {
    private UUID userid;
    private String lockType;
    private String reason;
    private LocalDate expirationDate;
}
