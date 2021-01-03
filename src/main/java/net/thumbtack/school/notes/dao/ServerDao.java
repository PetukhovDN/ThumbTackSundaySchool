package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;

public interface ServerDao {
    void clear();

    String makeAdmin(User user);

    User getUserByLogin(String login) throws NoteServerException;
}
