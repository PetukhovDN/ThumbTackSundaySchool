package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.mappers.SessionMapper;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionDaoImpl extends DaoImplBase implements SessionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    private final SqlSession sqlSession;

    public SessionDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public String logInUser(String login, String password) {
        LOGGER.info("DAO User with login {} login to server", login);
        String userToken;
        try {
            User user = getUserMapper(sqlSession).getUserByLogin(login, password);
            int userId = user.getId();
            SessionMapper currentSession = getSessionMapper(sqlSession);
            userToken = UUID.randomUUID().toString();
            if (!currentSession.getUserSession(userId).isEmpty()) {
                currentSession.logoutFromDatabase(userId);
                currentSession.loginToDatabase(userToken, userId);
            } else {
                getSessionMapper(sqlSession).loginToDatabase(userToken, userId);
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
