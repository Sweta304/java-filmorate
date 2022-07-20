package ru.yandex.practicum.filmorate.inMemory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component
@Getter
@Setter
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public User addUser(User user) throws UserAlreadyExistsException, ValidationException {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.get(user.getId()) != null) {
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

    @Override
    public User updateUser(User user) throws UserNotFoundException, ValidationException {
        if (users.get(user.getId()) == null) {
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

    @Override
    public List<User> getAllUsers() {
        log.info("текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("пользователь с id " + id + " не существует");
        }
        return user;
    }

    public long makeId() {
        return ++lastId;
    }
}
