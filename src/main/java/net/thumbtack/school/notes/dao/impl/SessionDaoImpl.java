package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionDaoImpl implements SessionDao {
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Override
    public String logInUser(String login, String password, Session session) throws NoteServerException {
        log.info("DAO User with login {} login to server", login);
        try {
            User user = userMapper.getUserByLogin(login, password);
            sessionMapper.startUserSession(session.getSessionId(), user.getId());
        } catch (NullPointerException ex) {
            log.error("User with login {} doesn't exists", login, ex);
            throw new NoteServerException(ExceptionErrorInfo.LOGIN_DOESNT_EXISTS, login);
        } catch (DuplicateKeyException ex) {
            log.error("User with login {} already logged in", login, ex);
            throw new NoteServerException(ExceptionErrorInfo.USER_ALREADY_LOGGED_IN, login);
        } catch (RuntimeException ex) {
            log.error("User with login {} can't login to server, ", login, ex);
            throw ex;
        }
        return session.getSessionId();
    }

    @Override
    public void logOutUser(Session session) {
        log.info("DAO User logout from server");
        try {
            sessionMapper.stopUserSession(session.getSessionId());
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }
}
