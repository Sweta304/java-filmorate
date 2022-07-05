package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Validated
public class User {
    private long id;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    private LocalDate birthday;
    @Email(message = "некорректный email")
    private String email;
    private final Set<Long> fiendsList = new HashSet<>();
    private FriendshipStatus friendshipStatus;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    public static boolean validate(User user) {
        boolean isValid = false;
        if (!(user.getEmail() == null
                || user.getEmail().isEmpty()
                || user.getEmail().isBlank()
                || !user.getEmail().contains("@")
                || user.getLogin() == null
                || user.getLogin().isEmpty()
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now()))
                || user.getId() < 0) {
            isValid = true;
        }
        return isValid;
    }

    public void addFriend(long id) {
        fiendsList.add(id);
    }

    public void deleteFriend(long id) {
        fiendsList.remove(id);
    }
}
