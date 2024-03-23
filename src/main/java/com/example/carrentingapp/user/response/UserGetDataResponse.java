package com.example.carrentingapp.user.response;

import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDataResponse {
    private UserBase user;
}
