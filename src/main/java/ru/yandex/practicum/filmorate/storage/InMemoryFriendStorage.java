package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InMemoryFriendStorage implements FriendStorage {

    private List<Friendship> friendships;

    public InMemoryFriendStorage() {
        friendships = new ArrayList<>();
    }

    @Override
    public void sendFriendRequest(User initiator, User recipient) {
        Friendship friendship = new Friendship(initiator, recipient, false);
        friendships.add(friendship);
    }

    //initiator - who want to delete friend from his friendList
    @Override
    public void deleteFriend(User initiator, User recipient) {
        Friendship friendship = new Friendship(recipient, initiator, true);//initiator confirmed recipient's request some time ago
        friendships.remove(friendship);
        initiator.getFriendsSet().remove(recipient.getId());
    }

    //initiator - who sent friendship request
    @Override
    public void confirmFriendRequest(User initiator, User recipient) {
        Friendship targetFriendship = new Friendship(initiator, recipient, false);
        Friendship friendship = friendships.stream().filter(user -> targetFriendship.equals(user)).findAny()
                .orElse(null);
        friendship.setStatus(true);
        recipient.getFriendsSet().add(initiator.getId());
    }

//    @Override
//    public List<Friendship> getUserFriendRequestsList(User user) {
//        return friendships.stream().filter(targetUser -> user.equals(targetUser.getRecipient())).collect(Collectors.toList());
//    }

}
