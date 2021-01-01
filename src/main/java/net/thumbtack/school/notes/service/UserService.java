package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    @Transactional
    UserInfoResponse registerUser(RegisterRequest userRequest, HttpServletResponse response) throws NoteServerException;

    @Transactional
    String loginUser(LoginRequest loginRequest, HttpServletResponse response) throws NoteServerException;

    @Transactional
    void logoutUser(String sessionToken) throws NoteServerException;

    @Transactional
    UserInfoResponse getUserInfo(String sessionToken) throws NoteServerException;

    @Transactional
    void leaveServer(LeaveServerRequest leaveRequest, String sessionToken) throws NoteServerException;
}
