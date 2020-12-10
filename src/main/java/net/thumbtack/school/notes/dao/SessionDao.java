package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;

public interface SessionDao {
    String logInUser(String login, String password, Session session) throws NoteServerException;

    void logOutUser(Session session);

    int getUserIdBySessionId(String sessionId) throws NoteServerException;
}
