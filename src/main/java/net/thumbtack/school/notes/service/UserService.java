package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.RegisterResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;

import javax.servlet.http.HttpSession;

public interface UserService {

    RegisterResponse registerUser(RegisterRequest userRequest, HttpSession userSession) throws NoteServerException;

    String loginUser(LoginRequest loginRequest, HttpSession userSession) throws NoteServerException;

    void logoutUser(HttpSession userSession) throws NoteServerException;
}
