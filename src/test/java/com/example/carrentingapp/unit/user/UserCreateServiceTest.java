package com.example.carrentingapp.unit.user;

import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.enums.Role;
import com.example.carrentingapp.user.service.UserCreateService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserCreateServiceTest {

    @Autowired
    private UserCreateService service;

    @Autowired
    private BaseUserRepository repository;

    @Test
    public void testCreateUser(){
        BaseUser user = service.createUser(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        BaseUser userFromDatabase = repository.findByEmail("jan@kowalski.pl").orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(Role.USER.name(), userFromDatabase.getRole().name());
        Assertions.assertEquals(userFromDatabase.toString(), user.toString());
    }

    @Test
    public void testCreateAdmin(){
        BaseUser user = service.createAdmin(
                "Jan",
                "Kowalski",
                "jan123@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        BaseUser userFromDatabase = repository.findByEmail("jan123@kowalski.pl").orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals( Role.ADMIN.name(), userFromDatabase.getRole().name());
        Assertions.assertEquals(userFromDatabase.toString(), user.toString());
    }

}