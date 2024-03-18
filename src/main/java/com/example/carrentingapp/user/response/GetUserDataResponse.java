package com.example.carrentingapp.user.response;

import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserDataResponse {
    private BaseUser user;
}
