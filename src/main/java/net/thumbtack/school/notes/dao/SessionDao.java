package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;

public interface SessionDao {
    String logInUser(String login, String password, String sessionToken) throws NoteServerException;

    void logOutUser(String sessionToken);

    int getUserIdBySessionId(String sessionId) throws NoteServerException;
}
