package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenreStorage {
    public List<Genres> getAllGenres();

    public Genres getGenreById(int id) throws GenreNotFoundException;
}
