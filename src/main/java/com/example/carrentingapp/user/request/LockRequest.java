package com.example.carrentingapp.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockRequest {
    private UUID userid;
    private String lockType;
    private String reason;
    private LocalDate expirationDate;
}
