package ru.yandex.practicum.filmorate.DAO;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Component
public class MpaDAO implements MpaStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MpaRating getMpaById(int id) throws MpaNotFoundException {
        String query = "select *\n" +
                "from MPA\n" +
                "where MPA_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        if (userRows.next()) {
            MpaRating mpaRating = jdbcTemplate.queryForObject(query, this::makeMpa, id);
            log.info("Найден рейтинг: {} {}", mpaRating.getId());
            return mpaRating;
        } else {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }

    public List<MpaRating> getAllMpa() {
        String query = "select *\n" +
                "from MPA\n";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    private MpaRating makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int mpaId = resultSet.getInt("mpa_id");
        String name = resultSet.getString("mpa_name");
        return MpaRating.builder().id(mpaId).name(name).build();
    }
}
