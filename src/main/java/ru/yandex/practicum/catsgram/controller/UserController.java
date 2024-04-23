package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

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
    public User findUserById(@PathVariable("userId") long userId) throws ConditionsNotMetException {
        if (userService.findUserById(userId).isPresent()) {
            return userService.findUserById(userId).get();
        } else {
            throw new ConditionsNotMetException("Нет User с заданным id");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create (@RequestBody User user) throws ConditionsNotMetException {
        return userService.create(user);
    }

    @PutMapping
    public User update (@RequestBody User user) throws ConditionsNotMetException, DuplicatedDataException {
        return userService.update(user);
    }
}
