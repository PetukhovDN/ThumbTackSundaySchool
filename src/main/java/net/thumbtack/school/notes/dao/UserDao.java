package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.params.UserRequestParam;

import java.util.List;

public interface UserDao {
    User registerUser(User user) throws NoteServerException;

    User getUserInfo(int userId) throws NoteServerException;

    void leaveNotesServer(int userId) throws NoteServerException;

    User editUserInfo(User userToUpdate);

    void changeUserStatus(User user);

    List<User> getAllUsers();

    void followUser(String userToken, String login);

    void ignoreUser(String userToken, String login);

    void stopFollowUser(String userToken, String login);

    void stopIgnoreUser(String userToken, String login);
}
