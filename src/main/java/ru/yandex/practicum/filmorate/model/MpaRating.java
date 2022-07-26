package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MpaRating {
    private int id;
    private String name;

    public MpaRating(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static boolean mpaValidation(int id) {
        boolean isValid = true;
        if (id < 0 || id > 5) {
            isValid = false;
        }
        return isValid;
    }
}
