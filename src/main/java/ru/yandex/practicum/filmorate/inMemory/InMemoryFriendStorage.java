package ru.yandex.practicum.filmorate.inMemory;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InMemoryFriendStorage implements FriendStorage {

    private List<Friendship> friendships;

    public InMemoryFriendStorage() {
        friendships = new ArrayList<>();
    }

    @Override
    public void sendFriendRequest(User initiator, User recipient) {
        Friendship friendship = new Friendship(initiator.getId(), recipient.getId(), false);
        friendships.add(friendship);
    }

    //initiator - who want to delete friend from his friendList
    @Override
    public void deleteFriend(User initiator, User recipient) {
        Friendship friendship = new Friendship(recipient.getId(), initiator.getId(), true);//initiator confirmed recipient's request some time ago
        friendships.remove(friendship);
        initiator.getFriendsSet().remove(recipient.getId());
    }

    //initiator - who sent friendship request
    @Override
    public void confirmFriendRequest(User initiator, User recipient) {
        Friendship targetFriendship = new Friendship(initiator.getId(), recipient.getId(), false);
        Friendship friendship = friendships.stream().filter(user -> targetFriendship.equals(user)).findAny()
                .orElse(null);
        friendship.setStatus(true);
        recipient.getFriendsSet().add(initiator.getId());
    }

    @Override
    public List<Friendship> getUserFriendRequestsList(User user) {
        return friendships.stream().filter(targetUser -> user.getId()==(targetUser.getRecipient())).collect(Collectors.toList());
    }

}
