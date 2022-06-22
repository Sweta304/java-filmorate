package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws UserAlreadyExistsException, ValidationException {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws UserNotFoundException, ValidationException {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        if (userStorage.getUsers().get(id) == null || userStorage.getUsers().get(friendId) == null) {
            throw new UserNotFoundException("пользователя для добавления друга не существует");
        }
        return userService.addFriend(userStorage.getUsers().get(id), userStorage.getUsers().get(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriend(userStorage.getUsers().get(id), userStorage.getUsers().get(friendId));
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable long id) {
        Set<Long> friendsIdList = userService.getFriends(userStorage.getUsers().get(id));
        Set<User> friendsList = new HashSet<>();
        if (friendsIdList != null) {
            friendsList = friendsIdList.stream()
                    .map(userStorage.getUsers()::get)
                    .collect(Collectors.toSet());
            return friendsList;
        } else {
            return friendsList;
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) throws UserNotFoundException {
        Set<Long> friendsIdList = userService.getCommonFriends(userStorage.getUsers().get(id), userStorage.getUsers().get(otherId));
        Set<User> friendsList = new HashSet<>();
        if (friendsIdList != null) {
            friendsList = friendsIdList.stream()
                    .map(userStorage.getUsers()::get)
                    .collect(Collectors.toSet());
        }
            return friendsList;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) throws UserNotFoundException {
        return userStorage.getUserById(id);
    }
}
