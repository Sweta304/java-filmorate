package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dictionary.Genre;


@Data
@Builder
public class Genres {
    private int id;
    private String name;

    public Genres(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static boolean genreValidation(int id) {
        boolean isValid = true;
        if (id < 0 || id > Genre.values().length) {
            isValid = false;
        }
        return isValid;
    }
}
