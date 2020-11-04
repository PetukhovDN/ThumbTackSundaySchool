package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.params.UserRequestParam;

import java.util.List;

public interface UserDao {
    String registerUser(User user);

    User getUserInfo(String userToken);

    void leaveNotesServer(String userToken, String password);

    User editUserInfo(String userToken,
                      String newFirstName,
                      String newLastName,
                      String newPatronymic,
                      String password,
                      String nePassword);

    void giveAdminRoot(String userToken, int userId);

    List<User> getUsersWithParams(String userToken, UserRequestParam param);

    void followUser(String userToken, String login);

    void ignoreUser(String userToken, String login);

    void stopFollowUser(String userToken, String login);

    void stopIgnoreUser(String userToken, String login);
}
