package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionDaoImpl implements SessionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionDaoImpl(UserMapper userMapper,
                          SessionMapper sessionMapper) {
        this.userMapper = userMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public String logInUser(String login, String password) {
        LOGGER.info("DAO User with login {} login to server", login);
        String userToken;
        try {
            User user = userMapper.getUserByLogin(login, password);
            int userId = user.getId();
            SessionMapper currentSession = sessionMapper;
            userToken = UUID.randomUUID().toString();
            if (!currentSession.getUserSession(userId).isEmpty()) {
                currentSession.logoutFromDatabase(userId);
                currentSession.loginToDatabase(userToken, userId);
            } else {
                sessionMapper.loginToDatabase(userToken, userId);
            }
        } catch (RuntimeException ex) {
            LOGGER.error("User with login {} can't login to server, ", login, ex);
            throw ex;
        }
        return userToken;
    }

    @Override
    public void logOutUser(String userToken) {

    }
}
