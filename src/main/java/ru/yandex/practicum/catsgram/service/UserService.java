package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        System.out.println( );
        return users.values();
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get((long)id));
    }

    public User create(User newUser) throws ConditionsNotMetException {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (findEmail(newUser)) {
            throw new ConditionsNotMetException("Этот имейл уже используется");
        } else {
            newUser.setId(getNextId());
            newUser.setRegistrationDate(Instant.now());
            users.put(newUser.getId(), newUser);
            return newUser;
        }
    }

    public User update(User newUser) throws ConditionsNotMetException, DuplicatedDataException {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if(findEmail(newUser)){
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if(newUser.getEmail()==null||newUser.getEmail().isBlank()
                ||newUser.getUsername()==null||newUser.getUsername().isBlank()
                ||newUser.getPassword()==null||newUser.getPassword().isBlank()){
            return users.get(newUser.getId());
        }
        users.replace(newUser.getId(), newUser);
        return newUser;
    }

    public Optional<User> findUserById(Long id){
        return Optional.ofNullable(users.get(id));
    }

    private boolean findEmail(User newUser) {
        for (User user : users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
