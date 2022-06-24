package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        return user;
    }

    public User deleteFriend(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(id);
        return user;
    }

    public Set<User> getCommonFriends(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        Set<Long> commonFriends;
        if (user.getFiendsList() == null || friend.getFiendsList() == null) {
            return null;
        }
        commonFriends = user.getFiendsList().stream()
                .filter(friend.getFiendsList()::contains)
                .collect(Collectors.toSet());

        Set<User> friendsSet = new HashSet<>();

        if (commonFriends != null) {
            friendsSet = commonFriends.stream()
                    .map(x -> {
                        try {
                            return getUserById(x);
                        } catch (UserNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        }
        return friendsSet;
    }

    public Set<User> getFriends(long id) throws UserNotFoundException {
        User user = getUserById(id);
        Set<Long> friendsIdSet = user.getFiendsList();
        Set<User> friendsSet = new HashSet<>();

        if (friendsIdSet != null) {
            friendsSet = friendsIdSet.stream()
                    .map(x -> {
                        try {
                            return getUserById(x);
                        } catch (UserNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        }
        return friendsSet;
    }

    public User addUser(User user) throws UserAlreadyExistsException, ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws UserNotFoundException, ValidationException {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) throws UserNotFoundException {
        return userStorage.getUserById(id);
    }

}
