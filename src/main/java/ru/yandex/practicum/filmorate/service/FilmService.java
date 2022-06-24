package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public long addLike(long filmId, long userId) throws FilmNotFoundException {
        Film film = getFilmById(filmId);
        film.addLike(userId);
        film.setRate(film.getRate() + 1);
        return film.getUsersLikes().size();
    }

    public long deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = getFilmById(filmId);
        if (userId <= 0) {
            throw new UserNotFoundException("пользователя с id " + userId + "не существует");
        }
        film.deleteLike(userId);
        film.setRate(film.getRate() - 1);
        return film.getUsersLikes().size();
    }

    public List<Film> getTopFilms(int filmQty) throws FilmNotFoundException {
        if (filmQty <= 0) {
            throw new FilmNotFoundException("передано некорректное кол-во фильмов");
        }
        List<Film> topTenFilms = filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(filmQty)
                .collect(Collectors.toList());
        return topTenFilms;
    }

    public Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws FilmNotFoundException, ValidationException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) throws FilmNotFoundException {
        return filmStorage.getFilmById(id);
    }
}
