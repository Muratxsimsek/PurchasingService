package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.emlakjet.purchasing.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<UserEntity, String> userMap = new ConcurrentHashMap<>();

    public UserService() {
        userMap.put(UserEntity.builder().firstName("John").lastName("Doe").email("john@doe.com") .build(), "pwd123");
        userMap.put(UserEntity.builder().firstName("Jane").lastName("Doe").email("jane@doe.com") .build(), "pwd12345");
    }

    public boolean authenticate(LoginRequest loginRequest) {

        UserEntity userEntity = UserEntity.builder().firstName(loginRequest.getFirstName()).lastName(loginRequest.getLastName()).email(loginRequest.getEmail()).build();

        return userMap.get(userEntity).equals(loginRequest.getPassword());
    }

    public List<String> getAllUserEmails(){
        List<UserEntity> userEntities = new ArrayList(userMap.keySet());
        return userEntities.stream().map(userEntity -> userEntity.getEmail()).collect(Collectors.toList());
    }
}
