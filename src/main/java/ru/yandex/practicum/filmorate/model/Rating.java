package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum Rating {
    G("G"),
    PG("PG"),
    PG_THIRTEEN("PG-13"),
    R("R"),
    NC_SEVENTEEN("NC-17");

    private String value;

    private Rating(String value) {
        this.value = value;
    }
}
