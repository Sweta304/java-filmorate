package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmDbStorage implements FilmStorage {
    @Override
    public Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException {
        return null;
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException, ValidationException {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film getFilmById(long id) throws FilmNotFoundException {
        return null;
    }
}
