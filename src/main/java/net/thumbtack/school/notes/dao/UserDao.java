package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;

import java.util.List;

public interface UserDao {
    User registerUser(User user) throws NoteServerException;

    User getUserInfo(int userId) throws NoteServerException;

    void leaveNotesServer(int userId) throws NoteServerException;

    User editUserInfo(User userToUpdate);

    void changeUserStatus(User user);

    List<User> getAllUsers();

    void followUser(int currentUserId, int userIdToFollow);

    void ignoreUser(int currentUserId, int userIdToFollow);

    void stopFollowUser(int currentUserId, int userIdToFollow);

    void stopIgnoreUser(int currentUserId, int userIdToFollow);

    User getUserByLogin(String login) throws NoteServerException;
}
