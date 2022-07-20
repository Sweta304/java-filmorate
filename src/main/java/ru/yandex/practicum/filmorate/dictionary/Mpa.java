package ru.yandex.practicum.filmorate.dictionary;

import lombok.Getter;

@Getter
public enum Mpa {
    G("G"),
    PG("PG"),
    PG_THIRTEEN("PG-13"),
    R("R"),
    NC_SEVENTEEN("NC-17");

    private String value;

    private Mpa(String value) {
        this.value = value;
    }
}
