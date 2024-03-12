package com.example.carrentingapp.integration.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.response.GetCarResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetCarFromApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CommonFunctionsProvider commonFunctionProvider;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testGetSingleCarAuthorized() throws URISyntaxException {
        final UUID id = commonFunctionProvider.createCarForTest();
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/" + id;

        String token = commonFunctionProvider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, testRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getCars().getId(), id);
    }

    @Test
    public void testGetSingleCarNotAuthorized(){
        final UUID id = commonFunctionProvider.createCarForTest();
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<GetCarResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    //todo: dokończyć testy
}
