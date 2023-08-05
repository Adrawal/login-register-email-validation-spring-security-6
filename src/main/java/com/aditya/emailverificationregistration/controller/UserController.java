package com.aditya.emailverificationregistration.controller;

import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


//users/getAllUsers

    @GetMapping("/getAllUsers")
    public List<Users> getUsers(){

        return userService.getAllUsers();

    }

}
