package net.thumbtack.school.notes.daoimpl;

import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.enums.RequestParam;
import net.thumbtack.school.notes.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class UserDaoImpl extends DaoImplBase implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public UUID registerUser(User user) {
        return null;
    }

    @Override
    public UUID logInUser(String login, String password) {
        return null;
    }

    @Override
    public String logOutUser(UUID token) {
        return null;
    }

    @Override
    public User getInfo(UUID token) {
        return null;
    }

    @Override
    public String leaveNotesServer(UUID token, String password) {
        return null;
    }

    @Override
    public User editUserIndo(UUID token, User user) {
        return null;
    }

    @Override
    public String giveAdminRoot(UUID token) {
        return null;
    }

    @Override
    public List<User> getUsersWithParams(UUID token, RequestParam... params) {
        return null;
    }

    @Override
    public String followUser(UUID token, String login) {
        return null;
    }

    @Override
    public String stopFollowUser(UUID token) {
        return null;
    }

    @Override
    public String ignoreUser(UUID token, String login) {
        return null;
    }

    @Override
    public String stopIgnoreUser(UUID token, String login) {
        return null;
    }
}
