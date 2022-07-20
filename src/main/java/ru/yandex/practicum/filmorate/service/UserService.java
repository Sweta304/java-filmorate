package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User sendFriendRequest(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        friendStorage.sendFriendRequest(user, friend);
        return user;
    }

    public User deleteFriend(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        friendStorage.deleteFriend(user, friend);
        return user;
    }

    public User confirmFriendRequest(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        friendStorage.confirmFriendRequest(user, friend);
        return user;
    }

    public List<Friendship> getUserFriendRequestsList(long id) throws UserNotFoundException {
        User user = getUserById(id);
        return friendStorage.getUserFriendRequestsList(user);
    }

    public Set<User> getCommonFriends(long id, long friendId) throws UserNotFoundException {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        Set<Long> commonFriends;
        if (user.getFriendsSet() == null || friend.getFriendsSet() == null) {
            return null;
        }
        commonFriends = user.getFriendsSet().stream()
                .filter(friend.getFriendsSet()::contains)
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
        Set<Long> friendsIdSet = user.getFriendsSet();
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

    public User addUser(User user) throws UserAlreadyExistsException, ValidationException, UserNotFoundException {
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
