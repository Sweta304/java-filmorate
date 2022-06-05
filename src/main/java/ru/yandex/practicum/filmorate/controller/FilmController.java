package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private List<Film> films = new ArrayList<>();
    private long lastId = 0;

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws FilmAlreadyExistsException, ValidationException {
        if (films.contains(film)) {
            throw new FilmAlreadyExistsException("фильм уже существует");
        } else if (!Film.validate(film)) {
            log.error("валидация фильма не пройдена");
            throw new ValidationException("данные о фильме указаны некорректно");
        } else {
            film.setId(makeId());
            films.add(film);
            log.info("добавлен новый фильм с id {}", film.getId());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws FilmNotFoundException, ValidationException {
        if (!films.contains(film)) {
            throw new FilmNotFoundException("такого фильма не существует");
        } else {
            if (Film.validate(film)) {
                films.remove(film);
                films.add(film);
            } else {
                throw new ValidationException("данные о фильме указаны некорректно");
            }
        }
        log.info("информация о фильме с id {} обновлена", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("текущее кол-во фильмов: {}", films.size());
        return films;
    }

    public long makeId() {
        return ++lastId;
    }

    public List<Film> getFilms() {
        return films;
    }
}
