package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.enums.RequestParam;
import net.thumbtack.school.notes.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    UUID registerUser(User user);

    UUID logInUser(String login, String password);

    String logOutUser(UUID token);

    User getUserByToken(UUID token);

    String leaveNotesServer(UUID token, String password);

    User editUserInfo(UUID token, User user);

    String giveAdminRoot(UUID token);

    List<User> getUsersWithParams(UUID token, RequestParam... params);

    String followUser(UUID token, String login);

    String stopFollowUser(UUID token);

    String ignoreUser(UUID token, String login);

    String stopIgnoreUser(UUID token, String login);
}
