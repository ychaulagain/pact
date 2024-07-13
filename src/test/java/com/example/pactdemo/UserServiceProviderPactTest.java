package com.example.pactdemo;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Use RANDOM_PORT here
@Provider("UserProvider")
@PactFolder("build/pacts")
public class UserServiceProviderPactTest {

    @Value("${local.server.port}")
    private int port;

    private static final Map<Long, User> users = new HashMap<>();

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port)); // Use the dynamically assigned port
        System.setProperty("pact.verifier.reporters", ""); // Disable Markdown reporter
    }

    @State("User with ID 1 exists")
    public void userWithId1Exists() {
        // Set up the state here, e.g., create a user with ID 1 in the database
        User user = new User(1L, "Leanne Graham", "Bret", "Sincere@april.biz");
        users.put(1L, user);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @RestController
    static class UserController {
        @GetMapping(value = "/users/1", produces = MediaType.APPLICATION_JSON_VALUE)
        public User getUser() {
            return users.get(1L);
        }
    }

    static class User {
        private Long id;
        private String name;
        private String username;
        private String email;

        // Constructors, getters, and setters

        public User() {
        }

        public User(Long id, String name, String username, String email) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
