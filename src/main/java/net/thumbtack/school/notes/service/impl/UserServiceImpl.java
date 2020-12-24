package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.SessionDao;
import net.thumbtack.school.notes.dao.UserDao;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.UserService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final SessionDao sessionDao;

    private Base64.Encoder enc = Base64.getEncoder();

    @Override
    @Transactional
    public ImmutablePair<UserInfoResponse, String> registerUser(RegisterRequest userRequest) throws NoteServerException {

        log.info("Trying to register user");
        User user = UserMapStruct.INSTANCE.requestRegisterUser(userRequest);
        User registeredUser = userDao.registerUser(user);
        String sessionToken = UUID.randomUUID().toString();
        sessionDao.logInUser(registeredUser.getLogin(), registeredUser.getPassword(), sessionToken);
        UserInfoResponse userInfoResponse = UserMapStruct.INSTANCE.responseRegisterUser(registeredUser);
        return new ImmutablePair<>(userInfoResponse, sessionToken);
    }

    @Override
    @Transactional
    public String loginUser(LoginRequest loginRequest) throws NoteServerException {

        log.info("Trying to login user");
        String sessionToken = UUID.randomUUID().toString();
        return sessionDao.logInUser(loginRequest.getLogin(), loginRequest.getPassword(), sessionToken);
    }

    @Override
    @Transactional
    public void logoutUser(String sessionToken) throws NoteServerException {

        log.info("Trying to logout user");
        sessionDao.logOutUser(sessionToken);
    }

    @Override
    @Transactional
    public UserInfoResponse getUserInfo(String sessionToken) throws NoteServerException {

        log.info("Trying to get user info");
        if (sessionToken == null) {
            log.error("User is not logged ib");
            throw new NoteServerException(ExceptionErrorInfo.USER_IS_NOT_LOGGED_IN, "Please log in");
        }
        int userId = sessionDao.getUserIdBySessionId(sessionToken);
        User user = userDao.getUserInfo(userId);
        return UserMapStruct.INSTANCE.responseRegisterUser(user);
    }

    @Override
    @Transactional
    public void leaveServer(LeaveServerRequest leaveRequest, String sessionToken) throws NoteServerException {

        log.info("Trying to delete user account");

        int userId = sessionDao.getUserIdBySessionId(sessionToken);
        sessionDao.logOutUser(sessionToken);
        userDao.leaveNotesServer(userId, leaveRequest.getPassword());
    }


}
