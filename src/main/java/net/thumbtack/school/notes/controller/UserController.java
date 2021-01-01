package net.thumbtack.school.notes.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping(value = "accounts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse registerUser(@RequestBody @Valid RegisterRequest registerRequest,
                                         HttpServletResponse response) throws NoteServerException {
        return userService.registerUser(registerRequest, response);
    }

    @PostMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody @Valid LoginRequest loginRequest,
                          HttpServletResponse response) throws NoteServerException {
        userService.loginUser(loginRequest, response);

    }

    @DeleteMapping(value = "sessions")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(@CookieValue(name = "JAVASESSIONID", required = false) String sessionToken) throws NoteServerException {
        userService.logoutUser(sessionToken);
    }

    @GetMapping(value = "account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getUserInfo(@CookieValue(name = "JAVASESSIONID", required = false) String sessionToken) throws NoteServerException {
        return userService.getUserInfo(sessionToken);
    }

    @DeleteMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void leaveServer(@RequestBody @Valid LeaveServerRequest leaveRequest,
                            @CookieValue(name = "JAVASESSIONID", required = false) String sessionToken) throws NoteServerException {
        userService.leaveServer(leaveRequest, sessionToken);
    }
}

