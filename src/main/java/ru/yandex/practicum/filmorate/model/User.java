package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@Validated
@ConfigurationProperties(prefix="application.properties")
public class User {
    private long id;

    @NotBlank
    @NotNull
    private String login;
    private String name;
    private LocalDate birthday;
    @Email(message = "некорректный email")
    private String email;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    public static boolean validate(User user) {
        boolean isValid = false;
        if (!(user.getEmail().isBlank()
                || user.getEmail().isEmpty()
                || user.getEmail() == null
                || !user.getEmail().contains("@")
                || user.getLogin().isEmpty()
                || user.getLogin().isBlank()
                || user.getLogin() == null
                || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now()))) {
            isValid = true;
        }
        return isValid;
    }
}
