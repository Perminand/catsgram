package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable("userId") Integer userId){
        return userService.findById(userId).orElseThrow(NullPointerException::new);
    }

    @PostMapping
    public User create (@RequestBody User user) throws ConditionsNotMetException {
        return userService.create(user);
    }

    @PutMapping
    public User update (@RequestBody User user) throws ConditionsNotMetException, DuplicatedDataException {
        return userService.update(user);
    }
}
