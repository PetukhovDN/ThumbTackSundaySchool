package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;

public interface SessionDao {
    Session logInUser(String login, String password, Session session) throws NoteServerException;

    void stopUserSession(String sessionToken) throws NoteServerException;

    Session getSessionBySessionId(String sessionId) throws NoteServerException;

    Session getSessionByUserId(int userId) throws NoteServerException;

    void updateSession(Session session);

}
