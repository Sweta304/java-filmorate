package ru.yandex.practicum.filmorate.DAO;

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
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Primary
@Component
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) throws UserAlreadyExistsException, ValidationException, UserNotFoundException {
        String query = "insert into FILMORATE_USER (USER_LOGIN, USER_NAME, BIRTHDAY, EMAIL)\n" +
                "values (?,?,?,?)";
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!User.validate(user)) {
            log.error("валидация пользователя не пройдена");
            throw new ValidationException("данные о пользователе указаны некорректно");
        } else if (getAllUsers().contains(user)) {
            throw new UserAlreadyExistsException("пользователь уже существует");
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"USER_ID"});
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getName());
                ps.setDate(3, Date.valueOf(user.getBirthday()));
                ps.setString(4, user.getEmail());
                return ps;
            }, keyHolder);
            Long userId = keyHolder.getKey().longValue();
            return getUserById(userId);
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, ValidationException {
        String query = "update FILMORATE_USER\n" +
                "set USER_LOGIN = ?,\n" +
                "    USER_NAME  = ?,\n" +
                "    EMAIL      = ?,\n" +
                "    BIRTHDAY   = ?\n" +
                "where USER_ID = ?";
        if (getUserById(user.getId()) == null) {
            throw new UserNotFoundException("такого пользователя не существует");
        } else {
            if (User.validate(user)) {
                jdbcTemplate.update(query, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
            } else {
                throw new ValidationException("данные о пользователе указаны некорректно");
            }
        }
        log.info("информация для пользователя с id {} обновлена", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "select * from FILMORATE_USER";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeUser(rs, rowNum));
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {
        String query = "select * from FILMORATE_USER where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        if (userRows.next()) {
            User user = jdbcTemplate.queryForObject(query, this::makeUser, id);
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("пользователь с id " + id + " не существует");
        }
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        Long userId = resultSet.getLong("user_id");
        Set friends = new FriendDbStorage(jdbcTemplate).getFriendListByUserId(userId);
        return User.builder()
                .id(userId)
                .login(resultSet.getString("user_login"))
                .name(resultSet.getString("user_name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .email(resultSet.getString("email"))
                .friendsSet(friends)
                .build();
    }
}
