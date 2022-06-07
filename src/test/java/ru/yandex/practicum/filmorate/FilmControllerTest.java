package ru.yandex.practicum.filmorate;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {

    private Film film = Film.builder()
            .name("Trainspotting")
            .description("такое вот описание")
            .releaseDate(LocalDate.of(1995, 1, 1))
            .duration(100)
            .build();

    @Test
    void contextLoads() {
    }

    @Test
    void descriptionLengthMoreThan200() {
        String generatedString = RandomStringUtils.randomAlphabetic(201);
        film.setDescription(generatedString);
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

    @Test
    void blankFilmName() {
        film.setName(" ");
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

    @Test
    void emptyFilmName() {
        film.setName("");
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

    @Test
    void nullFilmName() {
        film.setName(null);
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

    @Test
    void incorrectFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1000, 2, 3));
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

    @Test
    void filmDurationUnderZero() {
        film.setDuration(-1);
        FilmController filmController = new FilmController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, FilmAlreadyExistsException {
                        filmController.addFilm(film);
                    }
                });
        assertEquals("данные о фильме указаны некорректно", exception.getMessage());
    }

}
