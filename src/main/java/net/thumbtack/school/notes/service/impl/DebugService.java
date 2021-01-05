package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.ServerDaoImpl;
import net.thumbtack.school.notes.dto.response.user.ServerSettingsResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Debug service for testing application.
 * No need for user sessions
 * In production will be unavailable.
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class DebugService {
    final ServerDaoImpl serverDao;

    /**
     * Server settings, max user name length
     * Set in application properties
     */
    @Value("${max_name_length}")
    String maxNameLength;

    /**
     * Server settings, min user password length
     * Set in application properties
     */
    @Value("${min_password_length}")
    String minPasswordLength;

    /**
     * Server settings, time for which user session is alive
     * Set in application properties
     */
    @Value("${user_idle_timeout}")
    String userIdleTimeout;

    /**
     * Deletes all database information
     */
    public void clearDatabase() {
        serverDao.clear();
    }

    /**
     * Method to create user account without session (to use in tests)
     *
     * @return user account information
     */
    public User registerUser() {
        return serverDao.registerUser();
    }


    /**
     * Method to login user account (to use in tests)
     *
     * @param userId identifier of user to log in to the server
     * @return user session token
     */
    public String loginUser(int userId) {
        return serverDao.logInUser(userId);
    }

    /**
     * Method to make user administrator without session (to use in tests)
     *
     * @return session id of user in success
     */
    public User makeAdmin(User user) {
        return serverDao.makeAdmin(user);
    }

    /**
     * Method to get user information without user session (to use in tests)
     *
     * @param login login of the user about whom we receive information
     * @return user information
     * @throws NoteServerException will be thrown if user does`nt exists
     */
    public User getUserAccountInfoByLogin(String login) throws NoteServerException {
        return serverDao.getUserByLogin(login);
    }

    /**
     * Method to get current server settings
     *
     * @return response, which contains current server settings
     */
    public ServerSettingsResponse getServerSettings() {
        return new ServerSettingsResponse(
                maxNameLength,
                minPasswordLength,
                userIdleTimeout);
    }
}
