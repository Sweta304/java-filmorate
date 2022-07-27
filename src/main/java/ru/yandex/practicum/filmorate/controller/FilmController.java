package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws FilmAlreadyExistsException, ValidationException, MpaNotFoundException, GenreNotFoundException, FilmNotFoundException {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws FilmNotFoundException, ValidationException, MpaNotFoundException, GenreNotFoundException {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) throws FilmNotFoundException {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public long addLike(@PathVariable long id, @PathVariable long userId) throws FilmNotFoundException {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public long deleteLike(@PathVariable long id, @PathVariable long userId) throws FilmNotFoundException, UserNotFoundException {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") int count) throws FilmNotFoundException {
        return filmService.getTopFilms(count);
    }

}
