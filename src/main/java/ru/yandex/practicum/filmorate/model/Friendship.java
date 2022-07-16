package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    private User initiator;
    private User recipient;
    private boolean status;

    public Friendship(User initiator, User recipient, boolean status) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.status = status;
    }

}
