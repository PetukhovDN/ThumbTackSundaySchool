package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;

public interface ServerDao {
    void clear();

    User registerUser();

    String logInUser(int userId);

    User makeAdmin(User user);

    User getUserByLogin(String login) throws NoteServerException;
}
