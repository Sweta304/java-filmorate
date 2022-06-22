package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public long addLike(Film film, long userId) {
        film.addLike(userId);
        film.setRate(film.getRate() + 1);
        return film.getUsersLikes().size();
    }

    public long deleteLike(Film film, long userId) {
        film.deleteLike(userId);
        film.setRate(film.getRate() - 1);
        return film.getUsersLikes().size();
    }

    public List<Film> getTopFilms(int filmQty) {
        List<Film> topTenFilms = filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(filmQty)
                .collect(Collectors.toList());
        return topTenFilms;
    }
}
