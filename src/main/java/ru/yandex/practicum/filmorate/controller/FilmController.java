package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws FilmAlreadyExistsException, ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws FilmNotFoundException, ValidationException {
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) throws FilmNotFoundException {
        return filmStorage.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public long addLike(@PathVariable long id, @PathVariable long userId) throws FilmNotFoundException{
        if (filmStorage.getFilmById(id) == null) {
            throw new FilmNotFoundException("фильма не существует");
        }
        return filmService.addLike(filmStorage.getFilmById(id), userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public long deleteLike(@PathVariable long id, @PathVariable long userId) throws FilmNotFoundException, UserNotFoundException {
        if (filmStorage.getFilmById(id) == null) {
            throw new FilmNotFoundException("фильма не существует");
        }
        if (!filmStorage.getFilmById(id).getUsersLikes().contains(userId)) {
            throw new UserNotFoundException("пользователь не ставил лайк данному фильму");
        }
        return filmService.deleteLike(filmStorage.getFilmById(id), userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

}
