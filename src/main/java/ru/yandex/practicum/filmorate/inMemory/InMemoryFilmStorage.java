package ru.yandex.practicum.filmorate.inMemory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private long lastId = 0;

    @Override
    public Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException {
        if (films.get(film.getId()) != null) {
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

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException, ValidationException {
        if (films.get(film.getId()) == null) {
            throw new FilmNotFoundException("такого фильма не существует");
        } else {
            if (Film.validate(film)) {
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("данные о фильме указаны некорректно");
            }
        }
        log.info("информация о фильме с id {} обновлена", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("текущее кол-во фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) throws FilmNotFoundException {
        if (films.get(id) == null) {
            throw new FilmNotFoundException("фильма с id " + id + " не существует");
        }
        return films.get(id);
    }

    public long makeId() {
        return ++lastId;
    }

    @Override
    public long addLike(long filmId, long userId) throws FilmNotFoundException {
//        Film film = getFilmById(filmId);
//        film.getUsersLikes().add(userId);
//        film.setRate(film.getRate() + 1);
//        return film.getUsersLikes().size();
        return 1;
    }

    @Override
    public long deleteLike(long filmId, long userId) throws UserNotFoundException, FilmNotFoundException {
//        Film film = getFilmById(filmId);
//        if (userId <= 0) {
//            throw new UserNotFoundException("пользователя с id " + userId + "не существует");
//        }
//        film.getUsersLikes().remove(userId);
//        film.setRate(film.getRate() - 1);
//        return film.getUsersLikes().size();
        return 1;
    }

    @Override
    public List<Film> getTopFilms(int filmQty) throws FilmNotFoundException {
        if (filmQty <= 0) {
            throw new FilmNotFoundException("передано некорректное кол-во фильмов");
        }
        List<Film> topTenFilms = getAllFilms().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(filmQty)
                .collect(Collectors.toList());
        return topTenFilms;
    }
}
