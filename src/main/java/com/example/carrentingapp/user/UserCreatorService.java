package com.example.carrentingapp.user;

import com.example.carrentingapp.car.BaseCar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCreatorService {

    private final UserRepository userRepository;
    private final UserPasswordService passwordService;

    public BaseUser createUser(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        BaseUser user = new BaseUser(
                firstName,
                lastName,
                email,
                passwordService.encodePassword(password),
                dateOfBirth
        );

        userRepository.save(user);

        return user;
    }

    public BaseUser createAdmin(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        BaseUser user = new BaseUser(
                firstName,
                lastName,
                email,
                passwordService.encodePassword(password),
                dateOfBirth
        );
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        return user;
    }

}
