package ru.yandex.practicum.filmorate.DAO;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Component
public class GenresDAO implements GenreStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public GenresDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genres> getAllGenres() {
        String query = "select *\n" +
                "from GENRE\n";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    public Genres getGenreById(int id) throws GenreNotFoundException {
        String query = "select *\n" +
                "from GENRE\n" +
                "where GENRE_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        if (userRows.next()) {
            Genres genre = jdbcTemplate.queryForObject(query, this::makeGenre, id);
            log.info("Найден жанр: {} {}", genre.getId());
            return genre;
        } else {
            throw new GenreNotFoundException("Жанр не найден");
        }
    }

    public Genres makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int genreId = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");
        return Genres.builder().id(genreId).name(name).build();
    }
}
