package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@Primary
@Component
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) throws UserAlreadyExistsException, ValidationException {
        String query = "insert into FILMUSER (USER_LOGIN, USER_NAME, BIRTHDAY, EMAIL)\n" +
                "values (?,?,?,?)";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!User.validate(user)) {
            log.error("валидация пользователя не пройдена");
            throw new ValidationException("данные о пользователе указаны некорректно");
        } else {
            jdbcTemplate.update(query, user.getLogin(), user.getName(), user.getBirthday(), user.getEmail());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, ValidationException {
        String query = "update FILMUSER\n" +
                "set USER_LOGIN = ?,\n" +
                "    USER_NAME  = ?,\n" +
                "    EMAIL      = ?,\n" +
                "    BIRTHDAY   = ?\n" +
                "where USER_ID = ?";
        if (getUserById(user.getId()) == null) {
            throw new UserNotFoundException("такого пользователя не существует");
        } else {
            if (User.validate(user)) {
                jdbcTemplate.update(query, user.getLogin(), user.getName(), user.getBirthday(), user.getEmail());
            } else {
                throw new ValidationException("данные о пользователе указаны некорректно");
            }
        }
        log.info("информация для пользователя с id {} обновлена", user.getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "select * from filmUser";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeUser(rs,rowNum));
    }

//    private User makeUser(ResultSet rs) throws SQLException {
//        User user = new User(
//                rs.getLong("user_id"),
//                rs.getString("user_login"),
//                rs.getString("user_name"),
//                rs.getDate("birthday").toLocalDate(),
//                rs.getString("email")
//        );
//        return user;
//    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        String query = "select * from filmUser where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("user_id"), userRows.getString("user_login"));
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("user_login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate(),
                    userRows.getString("email"),
                    new TreeSet<>()
            );
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("user_login"))
                .name(resultSet.getString("user_name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .email(resultSet.getString("email"))
                .fiendsList(new HashSet<>())
                .build();
    }

}
