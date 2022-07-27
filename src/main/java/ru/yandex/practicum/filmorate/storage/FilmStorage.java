package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException, GenreNotFoundException, MpaNotFoundException, FilmNotFoundException;

    Film updateFilm(Film film) throws FilmNotFoundException, ValidationException, MpaNotFoundException, GenreNotFoundException;

    List<Film> getAllFilms();

    Film getFilmById(long id) throws FilmNotFoundException;

    long addLike(long filmId, long userId) throws FilmNotFoundException;

    long deleteLike(long filmId, long userId) throws UserNotFoundException, FilmNotFoundException;

    List<Film> getTopFilms(int filmQty) throws FilmNotFoundException;
}
