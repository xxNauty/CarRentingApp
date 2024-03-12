package com.example.carrentingapp.unit.token;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.configuration.jwt.JwtService;
import com.example.carrentingapp.user.BaseUser;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Test
    public void testToken(){
        BaseUser user = commonFunctionsProvider.createUser();

        String token = jwtService.generateToken(user);
        Assertions.assertFalse(token.isEmpty());

        Assertions.assertEquals(jwtService.extractUsername(token), user.getUsername());

        Double rand = jwtService.extractClaim(token, claims -> claims.get("rand", Double.class));
        Assertions.assertTrue(rand < 100);
    }

}
