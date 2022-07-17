package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    private long initiator;
    private long recipient;
    private boolean status;

    public Friendship(long initiator, long recipient, boolean status) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.status = status;
    }

}
