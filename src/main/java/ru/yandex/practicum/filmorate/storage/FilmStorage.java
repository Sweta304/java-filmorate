package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException;

    Film updateFilm(Film film) throws FilmNotFoundException, ValidationException;

    List<Film> getAllFilms();

    Film getFilmById(long id) throws FilmNotFoundException;
}
