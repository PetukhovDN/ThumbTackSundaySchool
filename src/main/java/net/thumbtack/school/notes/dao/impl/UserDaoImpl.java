package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.params.UserRequestParam;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserDaoImpl extends DaoImplBase implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public String registerUser(User user) {
        LOGGER.info("DAO insert User {} to Database", user);
        String userToken;
        try (SqlSession sqlSession = getSession()) {
            try {
                userToken = UUID.randomUUID().toString();
                user.setToken(UUID.fromString(userToken));
                getUserMapper(sqlSession).registerUser(user);
                user = getUserMapper(sqlSession).getUserByLogin(user.getLogin(), user.getPassword());
                getSessionMapper(sqlSession).loginToDatabase(userToken, user.getId());
            } catch (RuntimeException ex) {
                LOGGER.error("Can't insert User {} to Database, {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return userToken;
    }

    @Override
    public User getUserInfo(String userToken) {
        return null;
    }

    @Override
    public void leaveNotesServer(String userToken, String password) {

    }

    @Override
    public User editUserInfo(String userToken, String newFirstName, String newLastName, String newPatronymic, String password, String nePassword) {
        return null;
    }

    @Override
    public void giveAdminRoot(String userToken, int userId) {

    }

    @Override
    public List<User> getUsersWithParams(String userToken, UserRequestParam param) {
        return null;
    }

    @Override
    public void followUser(String userToken, String login) {

    }

    @Override
    public void ignoreUser(String userToken, String login) {

    }

    @Override
    public void stopFollowUser(String userToken, String login) {

    }

    @Override
    public void stopIgnoreUser(String userToken, String login) {

    }


}
