package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    public User addFriend(User user, User friend) {
        user.addFriend(friend.getId());
        friend.addFriend(user.getId());
        return user;
    }

    public User deleteFriend(User user, User friend) {
        user.deleteFriend(friend.getId());
        friend.deleteFriend(user.getId());
        return user;
    }

    public Set<Long> getCommonFriends(User user, User friend) throws UserNotFoundException{
        Set<Long> commonFriends;
        if (user.getFiendsList() == null || friend.getFiendsList() == null) {
            return null;
        }
        commonFriends = user.getFiendsList().stream()
                .filter(friend.getFiendsList()::contains)
                .collect(Collectors.toSet());
        return commonFriends;
    }

    public Set<Long> getFriends (User user) {
        return user.getFiendsList();
    }

}
