package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DAO.FilmDbStorage;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testAddUser() throws UserNotFoundException, ValidationException, UserAlreadyExistsException {
        User testUser = User.builder()
                .id(2)
                .login("dolores")
                .name("Nicky Name")
                .birthday(LocalDate.of(1990, 6, 3))
                .email("mail@gmail.ru")
                .friendsSet(new HashSet<>())
                .build();

        userStorage.addUser(testUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(2));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Nicky Name")
                );
    }

    @Test
    public void testFindUserById() throws UserNotFoundException {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertThat(users)
                .hasSize(2);
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException, ValidationException {
        User testUser = User.builder()
                .id(1)
                .login("Lana")
                .name("Sveta")
                .birthday(LocalDate.of(1990, 6, 3))
                .email("mail@mail.ru")
                .friendsSet(new HashSet<>())
                .build();
        userStorage.updateUser(testUser);
        Optional<User> user = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(user)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("login", "Lana")
                );
    }

    @Test
    public void testAddFilm() throws FilmAlreadyExistsException, ValidationException, GenreNotFoundException, MpaNotFoundException, FilmNotFoundException{
        Film testFilm = Film.builder()
                .id(3)
                .name("labore nulla")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1990, 6, 3))
                .duration(100)
                .rate(4)
                .genres(new ArrayList<>())
                .mpa(new MpaRating(1,"G"))
                .build();

        filmDbStorage.addFilm(testFilm);
        Optional<Film> userFilm = Optional.ofNullable(filmDbStorage.getFilmById(3));

        assertThat(userFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "labore nulla")
                );
    }

    @Test
    public void testUpdateFilm() throws ValidationException, GenreNotFoundException, MpaNotFoundException, FilmNotFoundException{
       Film testFilm = Film.builder()
                .id(1)
                .name("everest")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1990, 6, 3))
                .duration(100)
                .rate(4)
                .genres(new ArrayList<>())
                .mpa(new MpaRating(1,"G"))
                .build();

        filmDbStorage.updateFilm(testFilm);
        Optional<Film> userFilm = Optional.ofNullable(filmDbStorage.getFilmById(1));

        assertThat(userFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "everest")
                );
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmDbStorage.getAllFilms();
        assertThat(films)
                .hasSize(3);
    }

    @Test
    public void testGetFilmById() throws FilmNotFoundException{
        Optional<Film> userFilm = Optional.ofNullable(filmDbStorage.getFilmById(1));

        assertThat(userFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddLike() throws FilmNotFoundException{
        filmDbStorage.addLike(1L,1L);

        Set<Long> filmLikes = filmDbStorage.getUserLikesByFilmId(1);
        assertThat(filmLikes)
                .hasSize(1)
                .contains(1L);
    }

    @Test
    public void testDeleteLike() throws FilmNotFoundException, UserNotFoundException {
        filmDbStorage.deleteLike(1L, 1L);

        Set<Long> filmLikes = filmDbStorage.getUserLikesByFilmId(1);
        assertThat(filmLikes)
                .hasSize(0);
    }

    @Test
    public void testGetTopFilms() throws FilmAlreadyExistsException, ValidationException, GenreNotFoundException, MpaNotFoundException, FilmNotFoundException{
        Film film = Film.builder()
                .id(1)
                .name("everest")
                .description("Duis in consequat esse")
                .releaseDate(LocalDate.of(1990, 6, 3))
                .duration(100)
                .rate(4)
                .genres(new ArrayList<>())
                .mpa(new MpaRating(1,"G"))
                .build();

        List<Film> films = filmDbStorage.getTopFilms(1);
        assertThat(films)
                .hasSize(1).contains(film);

    }
}
