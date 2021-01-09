package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.params.UserRequestParam;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to work with user`s accounts and user`s sessions
 * In every method (except user account registration) check`s if session is alive
 * and updates session life time (except logging out from the server or leaving server) after successful request
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService {
    final UserDao userDao;
    final SessionDao sessionDao;

    /**
     * Time for which user session is alive
     * Set in application.properties
     */
    @Value("${user_idle_timeout}")
    int sessionLifeTime;

    public UserServiceImpl(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    /**
     * Method to register new user account on the server and automatically log in to the server
     * New User session will be created
     *
     * @param registerRequest contains registration information about user:
     *                        first name, last name, patronymic, login and password
     * @param newSessionId    session token for new user
     * @return new user information in success
     */
    @Override
    @Transactional
    public User registerUser(RegisterRequest registerRequest, String newSessionId) throws NoteServerException {
        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(registerRequest);
        User registeredUser = userDao.registerUser(user);
        Session userSession = new Session();
        userSession.setSessionId(newSessionId);
        userSession.setExpiryTime(sessionLifeTime);
        sessionDao.logInUser(registeredUser.getId(), userSession);
        return registeredUser;
    }

    /**
     * Method for the registered user to log in to the server
     * If user is already logged in, it will be new user session created
     *
     * @param loginRequest contains information about user to log in: user login and password
     * @param sessionId    user session token (to check if user is already online)
     * @param newSessionID user new session token (if user was offline)
     * @return user new session token in success
     */
    @Override
    @Transactional
    public String loginUser(LoginRequest loginRequest, String sessionId, String newSessionID) throws NoteServerException {
        log.info("Trying to login user");
        int userId = userDao.getUserByLogin(loginRequest.getLogin()).getId();
        checkIsPasswordCorrect(userId, loginRequest.getPassword());
        if (sessionDao.getSessionByUserId(userId) != null) {
            sessionDao.stopUserSession(sessionId);
        }
        Session userSession = new Session();
        userSession.setSessionId(newSessionID);
        userSession.setExpiryTime(sessionLifeTime);
        Session resultSession = sessionDao.logInUser(userId, userSession);
        sessionDao.updateSession(userSession);
        return resultSession.getSessionId();
    }

    /**
     * Method for user to log out from the server
     * Checks for not null session token (user must be logged in to the server if he wants lo log out)
     *
     * @param sessionId user session token
     */
    @Override
    @Transactional
    public void logoutUser(@NotNull String sessionId) throws NoteServerException {
        log.info("Trying to logout user");
        sessionDao.stopUserSession(sessionId);
    }

    /**
     * Method for user to get information about his account
     * Requires for user to be logged in to the server
     *
     * @param sessionId user session token
     * @return this user information in success
     * @throws NoteServerException if user is not logged in to the server, or user session token is incorrect
     */
    @Override
    @Transactional
    public User getUserInfo(@NotNull String sessionId) throws NoteServerException {
        log.info("Trying to get user info");
        try {
            Session userSession = sessionDao.getSessionBySessionId(sessionId);
            User user = userDao.getUserById(userSession.getUserId());
            sessionDao.updateSession(userSession);
            return user;
        } catch (NullPointerException ex) {
            log.error("No such session on the server");
            throw new NoteServerException(ExceptionErrorInfo.SESSION_DOES_NOT_EXISTS, "No such session on the server");
        }
    }

    /**
     * Method for user to leave server
     * User status will be changed for "deleted", but user account will remain on the server
     * Requires for user to be logged in to the server
     * Checks if password is correct for this user account
     *
     * @param leaveRequest contains information about this user: user login
     * @param sessionId    user session token
     * @throws NoteServerException if user is not logged in to the server
     */
    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to delete user account");
        try {
            Session session = sessionDao.getSessionBySessionId(sessionId);
            int userId = session.getUserId();
            String password = leaveRequest.getPassword();
            checkIsPasswordCorrect(userId, password);
            sessionDao.stopUserSession(sessionId);
            userDao.changeUserDeletedStatusToDeleted(userId);
        } catch (NullPointerException ex) {
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, sessionId);
        }
    }

    /**
     * Method to update user account information
     * Requires for user to be logged in to the server
     * Requires old password and checks if password is correct for this user account
     *
     * @param updateRequest contains user information to be updated: first name, last name, patronymic and password
     * @param sessionId     user session token
     * @return this user information in success
     */
    @Override
    @Transactional
    public User updateUserInfo(UpdateUserInfoRequest updateRequest, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to update user account");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        String oldPassword = updateRequest.getOldPassword();
        checkIsPasswordCorrect(userSession.getUserId(), oldPassword);
        User userToUpdate = UserMapStruct.INSTANCE.requestUpdateUser(updateRequest);
        userToUpdate.setId(userSession.getUserId());
        User resultUser = userDao.editUserInfo(userToUpdate);
        sessionDao.updateSession(userSession);
        return resultUser;
    }

    /**
     * Method to change status for user with the given id from USER to ADMIN
     * Requires for user who is changing to have ADMIN status already
     *
     * @param userId    id of the user, to whom the status will be changed
     * @param sessionId session id of user, who is changing another user status
     * @throws NoteServerException if user who is changing status have not enough rights for this
     */
    @Override
    @Transactional
    public void makeAdmin(int userId, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to make user admin");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        User currentUser = userDao.getUserById(userSession.getUserId());
        if (!currentUser.getUserStatus().equals(UserStatus.ADMIN)) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_ENOUGH_RIGHTS, currentUser.getUserStatus().toString());
        }
        User resultUser = userDao.getUserById(userId);
        resultUser.setUserStatus(UserStatus.ADMIN);
        userDao.changeUserStatus(resultUser);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to get users information in dependence of user request parameters
     *
     * @param requestParam contains parameters,  in which user wants to get user accounts from the server
     * @param sessionId    user session token
     * @return list of user accounts, depending on the user request parameters
     */

    //TODO: logic for parameters
    @Override
    @Transactional
    public List<UsersInfoResponse> getUsersInfo(UserRequestParam requestParam, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to get all users info");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        List<User> users = userDao.getAllUsers();
        ArrayList<UsersInfoResponse> usersResponse = new ArrayList<>();
        for (User user : users) {
            UsersInfoResponse response = UserMapStruct.INSTANCE.responseGetAllUsers(user);
            usersResponse.add(response);
        }
        sessionDao.updateSession(userSession);
        return usersResponse;
    }

    /**
     * Method to follow user
     * If started following, must stop ignoring
     *
     * @param login     login of the user who was followed
     * @param sessionId session token of the user, who started following
     */
    @Override
    @Transactional
    public void followUser(String login, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to start following for user with login {} ", login);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();
        userDao.stopIgnoreUser(currentUserId, userIdToFollow);
        userDao.followUser(currentUserId, userIdToFollow);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to ignore user
     * If started ignoring, must stop following
     *
     * @param login     login of the user who is being ignored
     * @param sessionId session token of the user, who started ignoring
     */
    @Override
    @Transactional
    public void ignoreUser(String login, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to start ignoring for user with login {} ", login);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();
        userDao.stopFollowUser(currentUserId, userIdToFollow);
        userDao.ignoreUser(currentUserId, userIdToFollow);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to stop following user
     *
     * @param login     login of the user who was no longer followed
     * @param sessionId session token of the user, who stopped following
     */
    @Override
    @Transactional
    public void stopFollowUser(String login, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to stop following for user with login {} ", login);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();
        userDao.stopFollowUser(currentUserId, userIdToFollow);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to stop ignoring user
     *
     * @param login     login of the user who was no longer ignored
     * @param sessionId session token of the user, who stopped ignoring
     */
    @Override
    @Transactional
    public void stopIgnoreUser(String login, @NotNull String sessionId) throws NoteServerException {
        log.info("Trying to stop ignoring for user with login {} ", login);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();
        userDao.stopIgnoreUser(currentUserId, userIdToFollow);
        sessionDao.updateSession(userSession);
    }


    /**
     * Method to check that given password is correct for account with this user id
     *
     * @param userId   id of the account, which is being checked
     * @param password given password, which is needed to be checked
     * @throws NoteServerException if the password is incorrect for account with this id
     */
    public void checkIsPasswordCorrect(int userId, String password) throws NoteServerException {
        log.info("Checking user password");
        if (!userDao.getUserById(userId).getPassword().equals(password)) {
            log.error("Wrong password {}", password);
            throw new NoteServerException(ExceptionErrorInfo.WRONG_PASSWORD, password);
        }
    }
}
