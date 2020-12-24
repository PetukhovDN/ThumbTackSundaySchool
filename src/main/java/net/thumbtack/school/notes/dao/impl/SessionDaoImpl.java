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
import net.thumbtack.school.notes.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class SessionDaoImpl implements SessionDao {
    UserMapper userMapper;
    SessionMapper sessionMapper;

    @Override
    public String logInUser(String login, String password, String sessionToken) throws NoteServerException {
        log.info("DAO User with login {} logging in to the server", login);
        try {
            User user = userMapper.getUserByLogin(login, password);
            sessionMapper.startUserSession(sessionToken, user.getId());
        } catch (NullPointerException ex) {
            log.error("User with login {} doesn't exists", login, ex);
            throw new NoteServerException(ExceptionErrorInfo.LOGIN_DOES_NOT_EXISTS, login);
        } catch (DuplicateKeyException ex) {
            log.error("User with login {} already logged in", login, ex);
            throw new NoteServerException(ExceptionErrorInfo.USER_ALREADY_LOGGED_IN, login);
        } catch (RuntimeException ex) {
            log.error("User with login {} can't login to server, ", login, ex);
            throw ex;
        }
        return sessionToken;
    }

    @Override
    public void logOutUser(String sessionToken) {
        log.info("DAO User logout from server");
        try {
            sessionMapper.stopUserSession(sessionToken);
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }

    @Override
    public int getUserIdBySessionId(String sessionId) throws NoteServerException {
        log.info("DAO User get ID from database");
        try {
            return sessionMapper.getUserIdBySessionId(sessionId);
        } catch (NullPointerException ex) {
            log.error("No session with id {} running on server", sessionId, ex);
            throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, ex.getMessage());
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }
}
