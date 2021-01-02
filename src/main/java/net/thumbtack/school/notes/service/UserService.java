package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    UserInfoResponse registerUser(RegisterRequest userRequest, String sessionId) throws NoteServerException;

    @Transactional
    String loginUser(LoginRequest loginRequest, String sessionId) throws NoteServerException;

    @Transactional
    void logoutUser(String sessionToken) throws NoteServerException;

    @Transactional
    UserInfoResponse getUserInfo(String sessionToken) throws NoteServerException;

    @Transactional
    void leaveServer(LeaveServerRequest leaveRequest, String sessionToken) throws NoteServerException;

    @Transactional
    UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateRequest, String sessionId) throws NoteServerException;
}
