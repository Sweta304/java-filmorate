package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Getter
@Component
public class FriendDbStorage implements FriendStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void sendFriendRequest(User initiator, User recipient) {
        String query = "insert into USER_FRIEND(user_id, friend_id, friendshipstatus)\n" +
                "values (?, ?, ?)";
        jdbcTemplate.update(query, initiator.getId(), recipient.getId(), false);
    }

    //initiator - who want to delete friend from his friendList
    @Override
    public void deleteFriend(User initiator, User recipient) {
        String query = "delete\n" +
                "from USER_FRIEND\n" +
                "where USER_ID = ?\n" +
                "  and FRIEND_ID = ?\n" +
                "  and FRIENDSHIPSTATUS = true";
        jdbcTemplate.update(query, recipient.getId(), initiator.getId());
    }

    //initiator - who received friend request
    @Override
    public void confirmFriendRequest(User initiator, User recipient) {
        String query = "update USER_FRIEND\n" +
                "set FRIENDSHIPSTATUS = true\n" +
                "where USER_ID = ?\n" +
                "  and FRIEND_ID = ?\n" +
                "  and FRIENDSHIPSTATUS = false";
        jdbcTemplate.update(query, recipient.getId(), initiator.getId());
    }

    @Override
    public List<Friendship> getUserFriendRequestsList(User user) {
        String query = "select *\n" +
                "from USER_FRIEND\n" +
                "where FRIEND_ID = ?";
        return jdbcTemplate.query(query, (rs, rowNum) -> createFriendship(rs, rowNum), user.getId());
    }

    private Friendship createFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .initiator(resultSet.getLong("user_id"))
                .recipient(resultSet.getLong("friend_id"))
                .status(resultSet.getBoolean("friendshipstatus"))
                .build();
    }
}
