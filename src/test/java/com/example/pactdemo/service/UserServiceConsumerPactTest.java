package com.example.pactdemo.service;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.pactdemo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactTestFor(providerName = "UserProvider", providerType = ProviderType.SYNCH)
public class UserServiceConsumerPactTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Pact(consumer = "UserConsumer")
    public V4Pact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("User with ID 1 exists")
                .uponReceiving("A request for user with ID 1")
                .path("/users/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\"id\":1,\"name\":\"Leanne Graham\",\"username\":\"Bret\",\"email\":\"Sincere@april.biz\"}")
                .toPact(V4Pact.class);
    }

    @Test
    void testGetUser(MockServer mockServer) {
        String baseUrl = mockServer.getUrl();
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/users/1", User.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Leanne Graham", response.getBody().getName());
        assertEquals("Bret", response.getBody().getUsername());
        assertEquals("Sincere@april.biz", response.getBody().getEmail());
    }
}
