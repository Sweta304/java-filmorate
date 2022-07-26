package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


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
        if (id < 0 || id > 6) {
            isValid = false;
        }
        return isValid;
    }
}
