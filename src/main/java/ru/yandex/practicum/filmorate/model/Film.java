package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200, message = "длина описания не должна превышать 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "длительность фильма не может быть отрицательной")
    private int duration;
    private long rate;
    private final Set<Long> usersLikes = new HashSet<>();
    private Genre genre;
    private Rating rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    public static boolean validate(Film film) {
        boolean isValid = false;
        if (!(film.getName() == null
                || film.getName().isBlank()
                || film.getDescription().length() > 200
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || film.getDuration() < 0
                || film.getId() < 0)) {
            isValid = true;
        }
        return isValid;
    }

    public void addLike(long id) {
        usersLikes.add(id);
    }

    public void deleteLike(long id) {
        usersLikes.remove(id);
    }
}
