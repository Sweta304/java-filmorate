package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws UserAlreadyExistsException, ValidationException {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws UserNotFoundException, ValidationException {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        return userService.sendFriendRequest(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable long id) throws UserNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) throws UserNotFoundException {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/requests/{friendId}")
    public User confirmFriendRequest(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        return userService.confirmFriendRequest(id, friendId);
    }
}
