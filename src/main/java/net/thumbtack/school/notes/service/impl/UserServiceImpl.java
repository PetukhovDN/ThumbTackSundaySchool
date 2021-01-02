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
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
    public UserInfoResponse registerUser(RegisterRequest userRequest, String sessionId) throws NoteServerException {

        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        Session userSession = new Session();
        userSession.setSessionId(sessionId);
        userSession.setExpiryTime(user_idle_timeout);
        sessionDao.logInUser(registeredUser.getLogin(), registeredUser.getPassword(), userSession);
        return UserMapStruct.INSTANCE.responseRegisterUser(registeredUser);
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
        sessionDao.stopUserSession(sessionId);
    }

    @Override
    @Transactional
    public UserInfoResponse getUserInfo(String sessionId) throws NoteServerException {

        log.info("Trying to get user info");
        if (sessionId == null) {
            log.error("User is not logged in");
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, "Please log in");
        }

        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        checkSessionExpired(userSession);
        User user = userDao.getUserInfo(userSession.getUserId());
        updateSession(userSession);
        return UserMapStruct.INSTANCE.responseRegisterUser(user);
    }

    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, String sessionId) throws NoteServerException {
        log.info("Trying to delete user account");

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


    public void checkIsPasswordCorrect(int userId, String password) throws NoteServerException {
        log.info("Checking user password");
        if (!userDao.getUserInfo(userId).getPassword().equals(password)) {
            log.error("Wrong password {}", password);
            throw new NoteServerException(ExceptionErrorInfo.WRONG_PASSWORD, password);
        }
    }

    public Session checkSessionExpired(Session userSession) throws NoteServerException {
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
        return userSession;
    }

    public void updateSession(Session userSession) throws NoteServerException {
        log.info("Updating session last access time");
        userSession.setLastAccessTime(LocalDateTime.now());
        sessionDao.updateSession(userSession);
    }
}
