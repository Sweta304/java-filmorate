package ru.yandex.practicum.filmorate.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dictionary.Genre;
import ru.yandex.practicum.filmorate.dictionary.Mpa;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Primary
@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final MpaDAO mpaDAO;
    private final GenresDAO genresDAO;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDAO mpaDAO, GenresDAO genresDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDAO = mpaDAO;
        this.genresDAO = genresDAO;
    }


    @Override
    public Film addFilm(Film film) throws FilmAlreadyExistsException, ValidationException, GenreNotFoundException, MpaNotFoundException, FilmNotFoundException {
        String query = "insert into FILM (FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA)\n" +
                "values (?, ?, ?, ?, ?, ?)";
        String genreQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID)\n" +
                "values (?, ?)";
        long filmId;
        if (getAllFilms().contains(film)) {
            throw new FilmAlreadyExistsException("фильм уже существует");
        } else if (!Film.validate(film)) {
            log.error("валидация фильма не пройдена");
            throw new ValidationException("данные о фильме указаны некорректно");
        } else if (film.getGenres() != null && film.getGenres().stream().filter(x -> Genres.genreValidation(x.getId())).collect(Collectors.toList()).size() < film.getGenres().size()) {
            throw new GenreNotFoundException("жанра не существует, введите значение от 1 до " + Genre.values().length);
        } else if (!MpaRating.mpaValidation(film.getMpa().getId())) {
            throw new MpaNotFoundException("Рейтинга не существует. Введите значение от 1 до " + Mpa.values().length);
        } else {
            jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId());
            filmId = getAllFilms()
                    .stream()
                    .filter(x -> x.equals(film))
                    .findFirst()
                    .get().getId();
            if (film.getGenres() != null) {
                film.getGenres().stream().distinct().forEach(x -> jdbcTemplate.update(genreQuery, filmId, x.getId()));
            }
            log.info("добавлен новый фильм с id {}", filmId);
        }
        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException, ValidationException, MpaNotFoundException, GenreNotFoundException {
        String query = "update FILM\n" +
                "  set FILM_NAME   = ?,\n" +
                "      DESCRIPTION = ?,\n" +
                "      RELEASEDATE = ?,\n" +
                "      DURATION    = ?,\n" +
                "      RATE        = ?,\n" +
                "      MPA         = ?" +
                "where FILM_ID = ? ";
        String deleteGenres = "delete\n" +
                "from FILM_GENRE\n" +
                "where FILM_ID = ?";
        String genreQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID)\n" +
                "values (?, ?)";
        if (getFilmById(film.getId()) == null) {
            throw new FilmNotFoundException("такого фильма не существует");
        } else if (!MpaRating.mpaValidation(film.getMpa().getId())) {
            throw new MpaNotFoundException("Рейтинга не существует. Введите значение от 1 до " + Mpa.values().length);
        } else if (film.getGenres() != null && film.getGenres().stream().filter(x -> Genres.genreValidation(x.getId())).collect(Collectors.toList()).size() < film.getGenres().size()) {
            throw new GenreNotFoundException("жанра не существует, введите значение от 1 до " + Genre.values().length);
        } else {
            if (Film.validate(film)) {
                jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
                if (film.getGenres() != null) {
                    jdbcTemplate.update(deleteGenres, film.getId());
                    film.getGenres().stream().distinct().forEach(x -> jdbcTemplate.update(genreQuery, film.getId(), x.getId()));
                }
            } else {
                throw new ValidationException("данные о фильме указаны некорректно");
            }
        }
        log.info("информация о фильме с id {} обновлена", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "select * from FILM";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    @Override
    public Film getFilmById(long id) throws FilmNotFoundException {
        String query = "select * from FILM where FILM_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        if (userRows.next()) {
            Film film = jdbcTemplate.queryForObject(query, this::makeFilm, id);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmNotFoundException("фильм с id " + id + " не существует");
        }
    }

    private Film makeFilm(ResultSet resultSet, int rownum) throws SQLException {
        Long filmId = resultSet.getLong("film_id");
        List<Genres> genresList = getFilmsGenresByFilmId(filmId);
        int mpaId = resultSet.getInt("mpa");
        String mpaName = Mpa.values()[mpaId - 1].getValue();
        MpaRating mpa = MpaRating.builder().id(mpaId).name(mpaName).build();
        return Film.builder()
                .id(filmId)
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .genres(genresList)
                .mpa(mpa)
                .build();
    }

    private Set<Long> getUserLikesByFilmId(long id) {
        String query = "select USER_ID\n" +
                "from FILM_LIKES\n" +
                "where FILM_ID = ?";
        return new HashSet(jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("user_id"), id));
    }

    private List<Genres> getFilmsGenresByFilmId(long id) {
        String query = "select GENRE.GENRE_ID, GENRE_NAME\n" +
                "from FILM_GENRE\n" +
                "join GENRE on FILM_GENRE.GENRE_ID = GENRE.GENRE_ID\n" +
                "where FILM_ID = ?";

        List<Genres> genres = jdbcTemplate.query(query, (rs, rowNum) -> genresDAO.makeGenre(rs, rowNum), id);
        return genres;
    }

    @Override
    public long addLike(long filmId, long userId) throws FilmNotFoundException {
        String query = "insert into FILM_LIKES (FILM_ID, USER_ID)\n" +
                "VALUES (?, ?)";
        String updateRating = "update FILM\n" +
                "set RATE = RATE + 1";
        if (!getAllFilms().contains(getFilmById(filmId))) {
            throw new FilmNotFoundException("Фильма не существует");
        } else {
            jdbcTemplate.update(query, filmId, userId);
            jdbcTemplate.update(updateRating);
        }
        return getUserLikesByFilmId(filmId).size();
    }

    @Override
    public long deleteLike(long filmId, long userId) throws UserNotFoundException, FilmNotFoundException {
        String query = "delete\n" +
                "from FILM_LIKES\n" +
                "where FILM_ID = ?\n" +
                "  and USER_ID = ?;";
        String updateRating = "update FILM\n" +
                "set RATE = RATE - 1";
        if (!getUserLikesByFilmId(filmId).contains(userId)) {
            throw new UserNotFoundException("пользователя с id " + userId + "не существует");
        } else if (!getAllFilms().contains(getFilmById(filmId))) {
            throw new FilmNotFoundException("Фильма не существует");
        } else {
            jdbcTemplate.update(query, filmId, userId);
            jdbcTemplate.update(updateRating);
        }
        return getUserLikesByFilmId(filmId).size();
    }

    @Override
    public List<Film> getTopFilms(int filmQty) throws FilmNotFoundException {
        if (filmQty <= 0) {
            throw new FilmNotFoundException("передано некорректное кол-во фильмов");
        }

        String query = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA from\n" +
                "(select F.*, count (FILM_LIKES.FILM_ID) as qty\n" +
                "from FILM F left outer join FILM_LIKES on FILM_LIKES.FILM_ID = F.FILM_ID\n" +
                "group by F.FILM_ID\n" +
                "order by qty desc)\n" +
                "where rownum <= ?";

        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs, rowNum), filmQty);
    }
}
