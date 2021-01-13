package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;

public interface SessionDao {
    Session logInUser(int userId, Session session) throws NoteServerException;

    void stopUserSession(int userId) throws NoteServerException;

    Session getSessionBySessionId(String sessionId) throws NoteServerException;

    Session getSessionByUserId(int userId) throws NoteServerException;

    void updateSession(Session session);
}
