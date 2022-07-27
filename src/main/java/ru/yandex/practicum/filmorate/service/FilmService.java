package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public long addLike(long filmId, long userId) throws FilmNotFoundException {
        return filmStorage.addLike(filmId, userId);
    }

    public long deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getTopFilms(int filmQty) throws FilmNotFoundException {
        return filmStorage.getTopFilms(filmQty);
    }

    public Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException, MpaNotFoundException, GenreNotFoundException, FilmNotFoundException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws FilmNotFoundException, ValidationException, MpaNotFoundException, GenreNotFoundException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) throws FilmNotFoundException {
        return filmStorage.getFilmById(id);
    }
}
