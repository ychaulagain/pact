package com.example.pactdemo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUserById(Long id) {
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/users/" + id, User.class);
    }
}

