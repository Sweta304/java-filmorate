package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    //    private final List<User> users = new ArrayList<>();
    private Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws UserAlreadyExistsException, ValidationException {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.get(user.getId())!=null) {
            throw new UserAlreadyExistsException("пользователь уже существует");
        } else if (!User.validate(user)) {
            log.error("валидация пользователя не пройдена");
            throw new ValidationException("данные о пользователе указаны некорректно");
        } else {
            user.setId(makeId());
            users.put(user.getId(), user);
            log.info("добавлен новый пользователь с id {}", user.getId());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws UserNotFoundException, ValidationException {
        if (users.get(user.getId())==null) {
            throw new UserNotFoundException("такого пользователя не существует");
        } else {
            if (User.validate(user)) {
                users.remove(user);
                users.put(user.getId(), user);
            } else {
                throw new ValidationException("данные о пользователе указаны некорректно");
            }
        }
        log.info("информация для пользователя с id {} обновлена", user.getId());
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public long makeId() {
        return ++lastId;
    }

}
