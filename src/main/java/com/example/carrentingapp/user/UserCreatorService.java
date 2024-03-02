package com.example.carrentingapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCreatorService {

    //todo skontrolować czy nie wystarczy package-private
    public record UserCreationData(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){}

    private final UserRepository userRepository;

    public void createUser(UserCreationData data){
        BaseUser user = new BaseUser(
                data.firstName,
                data.lastName,
                data.email,
                data.password, //todo haszowanie haseł
                data.dateOfBirth
        );

        userRepository.save(user);
    }

    public void createAdmin(UserCreationData data){
        BaseUser user = new BaseUser(
                data.firstName,
                data.lastName,
                data.email,
                data.password, //todo haszowanie haseł
                data.dateOfBirth
        );
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }

}
