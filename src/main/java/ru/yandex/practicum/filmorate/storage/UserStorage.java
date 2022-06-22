package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User addUser(User user) throws UserAlreadyExistsException, ValidationException;

    User updateUser(User user) throws UserNotFoundException, ValidationException;

    List<User> getAllUsers();

    Map<Long, User> getUsers();

    User getUserById(long id) throws UserNotFoundException;
}
