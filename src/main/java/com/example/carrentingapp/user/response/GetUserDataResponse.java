package com.example.carrentingapp.user.response;

import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserDataResponse {
    private BaseUser user;
}
