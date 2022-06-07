package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    //    private List<Film> films = new ArrayList<>();
    private Map<Long, Film> films = new HashMap<>();
    private long lastId = 0;

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws FilmAlreadyExistsException, ValidationException {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistsException("фильм уже существует");
        } else if (!Film.validate(film)) {
            log.error("валидация фильма не пройдена");
            throw new ValidationException("данные о фильме указаны некорректно");
        } else {
            film.setId(makeId());
            films.put(film.getId(), film);
            log.info("добавлен новый фильм с id {}", film.getId());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws FilmNotFoundException, ValidationException {
        if (!films.containsValue(film)) {
            throw new FilmNotFoundException("такого фильма не существует");
        } else {
            if (Film.validate(film)) {
//                films.remove(film);
                films.put(film.getId(), film);
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
        return new ArrayList<>(films.values());
    }

    public long makeId() {
        return ++lastId;
    }

}
