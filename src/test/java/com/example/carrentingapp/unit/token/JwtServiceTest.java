package com.example.carrentingapp.unit.token;

import com.example.carrentingapp.configuration.jwt.JwtService;
import com.example.carrentingapp.user.BaseUser;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService service;

    @Test
    public void testToken(){
        BaseUser user = new BaseUser(
                "Adam",
                "Nowak",
                "adam@nowak.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        String token = service.generateToken(user);
        Assertions.assertFalse(token.isEmpty());

        Double rand = service.extractClaim(token, claims -> claims.get("rand", Double.class));

        Assertions.assertEquals(service.extractUsername(token), user.getUsername());
        Assertions.assertTrue(rand < 100);
    }

}
