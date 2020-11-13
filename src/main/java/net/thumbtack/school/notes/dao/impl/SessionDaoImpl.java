package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionDaoImpl implements SessionDao {
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Override
    public String logInUser(String login, String password) {
        log.info("DAO User with login {} login to server", login);
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
            log.error("User with login {} can't login to server, ", login, ex);
            throw ex;
        }
        return userToken;
    }

    @Override
    public void logOutUser(String userToken) {

    }
}
