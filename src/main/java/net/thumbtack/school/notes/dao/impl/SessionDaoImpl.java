package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class SessionDaoImpl implements SessionDao {
    UserMapper userMapper;
    SessionMapper sessionMapper;

    @Override
    public Session logInUser(String login, String password, Session session) throws NoteServerException {
        log.info("DAO User with login {} logging in to the server", login);
        try {
            User user = userMapper.getUserByLoginAndPassword(login, password);
            if (sessionMapper.getSessionByUserId(user.getId()) != null) {
                sessionMapper.stopUserSession(user.getId());
            }
            sessionMapper.startUserSession(session, user.getId());
        } catch (NullPointerException ex) {
            log.error("User with login {} doesn't exists", login, ex);
            throw new NoteServerException(ExceptionErrorInfo.LOGIN_DOES_NOT_EXISTS, login);
        } catch (RuntimeException ex) {
            log.error("User with login {} can't login to server, ", login, ex);
            throw ex;
        }
        return session;
    }

    @Override
    public void stopUserSession(String sessionId) throws NoteServerException {
        log.info("DAO User logout from server");
        try {
            Session session = sessionMapper.getSessionBySessionId(sessionId);
            if (session == null) {
                log.error("No session with id {} running on server", sessionId);
                throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
            }
            sessionMapper.stopUserSession(session.getUserId());
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }

    @Override
    public Session getSessionBySessionId(String sessionId) throws NoteServerException {
        log.info("DAO Get user session from database");
        try {
            Session session = sessionMapper.getSessionBySessionId(sessionId);
            if (session == null) {
                log.error("No session with id {} running on server", sessionId);
                throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
            }
            return session;
        } catch (RuntimeException ex) {
            log.error("Can't get user session from server, ", ex);
            throw ex;
        }
    }

    @Override
    public Session getSessionByUserId(int userId) throws NoteServerException {
        log.info("DAO Get user session from database");
        try {
            Session session = sessionMapper.getSessionByUserId(userId);
            if (session == null) {
                log.error("No session for user with id {} running on server", userId);
                throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
            }
            return session;
        } catch (RuntimeException ex) {
            log.error("Can't get user session from server, ", ex);
            throw ex;
        }
    }

    @Override
    public void updateSession(Session session) {
        log.info("DAO update user session");
        try {
            sessionMapper.updateSessionLastAccessTime(session);
        } catch (RuntimeException ex) {
            log.error("Can't update user session, ", ex);
            throw ex;
        }
    }
}
