package com.example.ms.userservice;

import com.example.ms.userservice.entity.UserEntity;
import com.example.ms.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void saveUser(UserEntity userService) {
        repository.save(userService);
    }

    public void delete(UserEntity userService) {
        repository.delete(userService);
    }
}
