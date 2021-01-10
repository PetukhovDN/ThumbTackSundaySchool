package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.params.UserRequestParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    User registerUser(RegisterRequest userRequest, String sessionId) throws NoteServerException;

    @Transactional
    String loginUser(LoginRequest loginRequest, String sessionId, String newSessionID) throws NoteServerException;

    @Transactional
    void logoutUser(String sessionToken) throws NoteServerException;

    @Transactional
    User getUserInfo(String sessionToken) throws NoteServerException;

    @Transactional
    void leaveServer(LeaveServerRequest leaveRequest, String sessionToken) throws NoteServerException;

    @Transactional
    UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateRequest, String sessionId) throws NoteServerException;

    @Transactional
    void makeAdmin(int userId, String sessionId) throws NoteServerException;

    @Transactional
    List<UsersInfoResponse> getUsersInfo(UserRequestParam userRequestParam, String sessionId) throws NoteServerException;

    @Transactional
    void followUser(String login, String sessionId) throws NoteServerException;

    @Transactional
    void ignoreUser(String login, String sessionId) throws NoteServerException;

    @Transactional
    void stopFollowUser(String login, String sessionId) throws NoteServerException;

    @Transactional
    void stopIgnoreUser(String login, String sessionId) throws NoteServerException;
}
