package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@Validated
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
    private MpaRating mpa;
    private List<Genres> genres;
    private final Set<Long> usersLikes = new HashSet<>();


    public Film(long id, String name, String description, LocalDate releaseDate, int duration, long rate, MpaRating mpa, List<Genres> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration
                && Objects.equals(name, film.name)
                && Objects.equals(description, film.description)
                && Objects.equals(releaseDate, film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration, rate);
    }
}
