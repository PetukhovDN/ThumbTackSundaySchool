package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.model.Session;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DataAccessObject to work with user sessions
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class SessionDaoImpl implements SessionDao {
    final SessionMapper sessionMapper;

    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    /**
     * Method to save user session to the database
     *
     * @param userId  user identifier
     * @param session user session token
     * @return user session token in success
     */
    @Override
    public Session logInUser(int userId, Session session) {
        log.info("DAO User with id {} logging in to the server", userId);
        try {
            sessionMapper.startUserSession(session, userId);
        } catch (RuntimeException ex) {
            log.error("User with id {} can't login to server, ", userId, ex);
            throw ex;
        }
        return session;
    }

    /**
     * Method to stop user session
     *
     * @param sessionId user session token
     * @throws NoteServerException if there is no saved in database session with this session token
     */
    @Override
    public void stopUserSession(String sessionId) throws NoteServerException {
        log.info("DAO User logout from server");
        try {
            sessionMapper.stopUserSession(sessionId);
        } catch (NullPointerException ex) {
            log.error("No session with id {} running on server", sessionId);
            throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }

    /**
     * Method to get session from database by session token
     *
     * @param sessionId user session token
     * @return user session token in success
     */
    @Override
    public Session getSessionBySessionId(String sessionId) throws NoteServerException {
        log.info("DAO Get user session from database");
        try {
            Session userSession = sessionMapper.getSessionBySessionId(sessionId);
            if (sessionExpired(userSession)) {
                throw new NoteServerException(ExceptionErrorInfo.SESSION_EXPIRED, "Session with id " + sessionId + " expired");
            }
            return userSession;
        } catch (RuntimeException ex) {
            log.error("Can't get user session from server, ", ex);
            throw ex;
        }
    }

    /**
     * Method to get session from database by user identifier
     *
     * @param userId user identifier
     * @return user session token in success
     */
    @Override
    public Session getSessionByUserId(int userId) {
        log.info("DAO Get user session from database");
        try {
            return sessionMapper.getSessionByUserId(userId);
        } catch (RuntimeException ex) {
            log.error("Can't get user session from server, ", ex);
            throw ex;
        }
    }

    /**
     * Method to update the lifetime of the session
     *
     * @param session session token
     */
    @Override
    public void updateSession(Session session) {
        log.info("DAO update user session");
        try {
            session.setLastAccessTime(LocalDateTime.now());
            sessionMapper.updateSessionLastAccessTime(session);
        } catch (RuntimeException ex) {
            log.error("Can't update user session, ", ex);
            throw ex;
        }
    }

    /**
     * Method to check is session alive
     *
     * @param session user session to check
     * @return true if session expired
     */
    public boolean sessionExpired(Session session) throws NoteServerException {
        log.info("Checking if session is expired");
        try {
            long sessionStartTimeInSec = session.getLastAccessTime()
                    .atZone(ZoneId.of("Asia/Omsk"))
                    .toInstant()
                    .toEpochMilli() / 1000;
            long currentTimeInSec = LocalDateTime.now().atZone(ZoneId.of("Asia/Omsk")).toInstant().toEpochMilli() / 1000;
            return currentTimeInSec > sessionStartTimeInSec + session.getExpiryTime();
        } catch (NullPointerException ex) {
            log.error("No such session on the server");
            throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
        }
    }
}
