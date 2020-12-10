package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

public interface UserService {

    @Transactional
    UserInfoResponse registerUser(RegisterRequest userRequest, HttpSession userSession) throws NoteServerException;

    @Transactional
    String loginUser(LoginRequest loginRequest, HttpSession userSession) throws NoteServerException;

    @Transactional
    void logoutUser(HttpSession userSession) throws NoteServerException;

    @Transactional
    UserInfoResponse getUserInfo(HttpSession userSession) throws NoteServerException;

    @Transactional
    void leaveServer(LeaveServerRequest leaveRequest, HttpSession userSession) throws NoteServerException;
}
