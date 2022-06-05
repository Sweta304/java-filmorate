package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.net.http.HttpClient;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    private User user = User.builder().login("Sveta")
            .name("Svetlana")
            .birthday(LocalDate.of(1990, 06, 03))
            .email("box@ya.ru")
            .build();
    private Film film = Film.builder()
            .name("Trainspotting")
            .description("такое вот описание")
            .releaseDate(LocalDate.of(1995, 1, 1))
            .duration(100)
            .build();

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private HttpClient client = HttpClient.newHttpClient();

    @Test
    void contextLoads() {
    }

    @Test
    void blankEmail() {
        user.setEmail(" ");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void emptyEmail() {
        user.setEmail("");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void emailWithoutAt() {
        user.setEmail("box");
        UserController userController = new UserController();
        try {
            userController.addUser(user);
        } catch (UserAlreadyExistsException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(userController.getUsers().isEmpty(),"добавлен пользователь с некорректным email");
    }

//    @Test
//    void incorrectEmailFormat() {
//        user.setEmail("box@");
//        UserController userController = new UserController();
//        try {
//            userController.addUser(user);
//        } catch (UserAlreadyExistsException | ValidationException e) {
//            System.out.println(e.getMessage());
//        }
//        assertTrue(userController.getUsers().isEmpty(), "добавлен пользователь с некорректным форматом email");
//    }

    @Test
    void nullEmail() {
        user.setEmail(null);
        UserController userController = new UserController();
        try {
            userController.addUser(user);
        } catch (ValidationException | UserAlreadyExistsException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(userController.getUsers().isEmpty(),"добавлен пользователь с незаполненным email");
    }

    @Test
    void loginIsBlank() {
        user.setLogin(" ");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void loginIsEmpty() {
        user.setLogin("");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void loginIsNull() {
        user.setLogin(null);
        UserController userController = new UserController();
        try {
            userController.addUser(user);
        } catch (ValidationException | UserAlreadyExistsException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(userController.getUsers().isEmpty(),"добавлен пользователь с незаполненным логином");
    }

    @Test
    void incorrectBirthDay() {
        user.setBirthday(LocalDate.now().plusDays(1));
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void descriptionLengthMoreThan200() {
        String generatedString = RandomStringUtils.randomAlphabetic(201);
        film.setDescription(generatedString);
        FilmController filmController = new FilmController();
        try {
            filmController.addFilm(film);
        } catch (FilmAlreadyExistsException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(filmController.getFilms().isEmpty(),"добавлен фильм со слишком длинным описанием");
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
    void IncorrectFilmReleaseDate() {
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

//    @Test
//    void sendRequest() {
//        user.setEmail("");
//        RestTemplate restTemplate = new RestTemplate();
//        String requestBody = user.toString();
//        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody);
//        ResponseEntity<String> responseEntity = restTemplate
//                .exchange("http://localhost:8080/users", HttpMethod.POST, httpEntity, String.class);
//        assertEquals(500,responseEntity.getStatusCode(),"некорректный статус-код");
//    }

}
