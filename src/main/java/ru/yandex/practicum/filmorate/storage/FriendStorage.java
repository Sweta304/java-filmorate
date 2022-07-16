package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public interface FriendStorage {

    public void sendFriendRequest(User initiatorId, User recipientId);

    public void deleteFriend(User initiatorId, User recipientId);

    public void confirmFriendRequest(User initiatorId, User recipientId);

//    public List<Friendship> getUserFriendRequestsList(User user);

}
