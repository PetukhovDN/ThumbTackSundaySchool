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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionDaoImpl implements SessionDao {
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Value("${user_idle_timeout}")
    private int user_idle_timeout;


    @Override
    public String logInUser(String login, String password, Session session) throws NoteServerException {
    	// REVU логика в DAO
    	// DAO лишь переносчик информации в/из БД
    	// логика должна быть в сервисе
        log.info("DAO User with login {} login to server", login);
        try {
        	// REVU не надо проверять, есть ли логин
        	// проверка вернула, что нет, а через миллисекунду уже есть
        	// просто пробуйте добавить юзера с этим логином и ловите DuplicateKeyException
            if (checkLoginExists(login)) {
                User user = userMapper.getUserByLogin(login, password);
                sessionMapper.loginToDatabase(session.getSessionId(), user.getId());
            } else {
                throw new NoteServerException(ExceptionErrorInfo.LOGIN_DOESNT_EXISTS, login);
            }
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
            sessionMapper.logoutFromDatabase(session.getSessionId());
        } catch (RuntimeException ex) {
            log.error("User can't logout from server, ", ex);
            throw ex;
        }
    }

    public boolean checkLoginExists(String login) {
        log.info("Checking if login {} exists", login);
        try {
            if (userMapper.checkLoginExists(login) != null) {
                return true;
            }
        } catch (RuntimeException ex) {
            log.error("Cant get login from DataBase", ex);
            throw ex;
        }
        return false;
    }

    public boolean checkIsSessionValid(String sessionId) {
        log.info("Checking if session with id: {} is valid", sessionId);
        try {
            Session currentSession = sessionMapper.checkSessionExpired(sessionId);
            return currentSession.getLastAccessTime() + user_idle_timeout < new Date().getTime();
        } catch (RuntimeException ex) {
            log.error("Cant get session from DataBase", ex);
            throw ex;
        }
    }
}
