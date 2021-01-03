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
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService {
    final UserDao userDao;
    final SessionDao sessionDao;
    final String JAVASESSIONID = "JAVASESSIONID";

    @Value("${user_idle_timeout}")
    int user_idle_timeout;

    public UserServiceImpl(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest userRequest, String sessionId) throws NoteServerException {
        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        Session userSession = new Session();
        userSession.setSessionId(sessionId);
        userSession.setExpiryTime(user_idle_timeout);
        sessionDao.logInUser(registeredUser.getLogin(), registeredUser.getPassword(), userSession);
        return registeredUser;
    }

    @Override
    @Transactional
    public String loginUser(LoginRequest loginRequest, String sessionId) throws NoteServerException {
        log.info("Trying to login user");
        Session userSession = new Session();
        userSession.setSessionId(sessionId);
        userSession.setExpiryTime(user_idle_timeout);
        Session resultSession = sessionDao.logInUser(loginRequest.getLogin(), loginRequest.getPassword(), userSession);
        updateSession(resultSession);
        return resultSession.getSessionId();
    }

    @Override
    @Transactional
    public void logoutUser(String sessionId) throws NoteServerException {
        log.info("Trying to logout user");
        checkSessionIdNotNull(sessionId);
        sessionDao.stopUserSession(sessionId);
    }

    @Override
    @Transactional
    public User getUserInfo(String sessionId) throws NoteServerException {
        log.info("Trying to get user info");
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        User user = userDao.getUserInfo(userSession.getUserId());
        updateSession(userSession);
        return user;
    }

    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, String sessionId) throws NoteServerException {
        log.info("Trying to delete user account");
        checkSessionIdNotNull(sessionId);
        int userId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        String password = leaveRequest.getPassword();
        checkIsPasswordCorrect(userId, password);
        sessionDao.stopUserSession(sessionId);
        userDao.leaveNotesServer(userId);
    }

    @Override
    @Transactional
    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateRequest, String sessionId) throws NoteServerException {
        log.info("Trying to update user account");
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        String oldPassword = updateRequest.getOldPassword();
        checkIsPasswordCorrect(userSession.getUserId(), oldPassword);
        User userToUpdate = UserMapStruct.INSTANCE.requestUpdateUser(updateRequest);
        userToUpdate.setId(userSession.getUserId());
        User resultUser = userDao.editUserInfo(userToUpdate);
        updateSession(userSession);
        return UserMapStruct.INSTANCE.responseUpdateUserInfo(resultUser);
    }

    @Override
    @Transactional
    public void makeAdmin(int userId, String sessionId) throws NoteServerException {
        log.info("Trying to make user admin");
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        User currentUser = userDao.getUserInfo(userSession.getUserId());
        if (!currentUser.getUserStatus().equals(UserStatus.ADMIN)) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_ENOUGH_RIGHTS, currentUser.getUserStatus().toString());
        }
        User resultUser = userDao.getUserInfo(userId);
        resultUser.setUserStatus(UserStatus.ADMIN);
        userDao.changeUserStatus(resultUser);
    }

    @Override
    @Transactional
    public ArrayList<UsersInfoResponse> getAllUsersInfo(UserRequestParam userRequestParam, String sessionId) throws NoteServerException {
        log.info("Trying to get all users info");
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        List<User> users = userDao.getAllUsers();

        //logic

        ArrayList<UsersInfoResponse> usersResponse = new ArrayList<>();
        for (User user : users) {
            UsersInfoResponse response = UserMapStruct.INSTANCE.responseGetAllUsers(user);
            usersResponse.add(response);
        }
        //logic

        return usersResponse;
    }

    @Override
    @Transactional
    public void followUser(String login, String sessionId) throws NoteServerException {
        log.info("Trying to start following for user with login {} ", login);
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();

        userDao.followUser(currentUserId, userIdToFollow);
    }

    @Override
    @Transactional
    public void ignoreUser(String login, String sessionId) throws NoteServerException {
        log.info("Trying to start ignoring for user with login {} ", login);
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();

        userDao.ignoreUser(currentUserId, userIdToFollow);
    }

    @Override
    @Transactional
    public void stopFollowUser(String login, String sessionId) throws NoteServerException {
        log.info("Trying to stop following for user with login {} ", login);
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();

        userDao.stopFollowUser(currentUserId, userIdToFollow);
    }

    @Override
    @Transactional
    public void stopIgnoreUser(String login, String sessionId) throws NoteServerException {
        log.info("Trying to stop ignoring for user with login {} ", login);
        checkSessionIdNotNull(sessionId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        int currentUserId = userSession.getUserId();
        int userIdToFollow = userDao.getUserByLogin(login).getId();

        userDao.stopIgnoreUser(currentUserId, userIdToFollow);
    }


    public void checkIsPasswordCorrect(int userId, String password) throws NoteServerException {
        log.info("Checking user password");
        if (!userDao.getUserInfo(userId).getPassword().equals(password)) {
            log.error("Wrong password {}", password);
            throw new NoteServerException(ExceptionErrorInfo.WRONG_PASSWORD, password);
        }
    }

    public void checkSessionIdNotNull(String sessionId) throws NoteServerException {
        if (sessionId == null) {
            log.error("User is not logged in");
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, "Please log in");
        }
    }

    public void checkSessionExpired(Session userSession) throws NoteServerException {
        log.info("Checking if session expired");
        long sessionStartTimeInSec = userSession
                .getLastAccessTime()
                .atZone(ZoneId.of("Asia/Omsk"))
                .toInstant()
                .toEpochMilli() / 1000;
        long currentTimeInSec = LocalDateTime.now().atZone(ZoneId.of("Asia/Omsk")).toInstant().toEpochMilli() / 1000;
        if (currentTimeInSec > sessionStartTimeInSec + user_idle_timeout) {
            log.info("Session expired");
            sessionDao.stopUserSession(userSession.getSessionId());
            throw new NoteServerException(ExceptionErrorInfo.SESSION_EXPIRED, "Please log in");
        }
    }

    public void updateSession(Session userSession) throws NoteServerException {
        log.info("Updating session last access time");
        userSession.setLastAccessTime(LocalDateTime.now());
        sessionDao.updateSession(userSession);
    }
}
