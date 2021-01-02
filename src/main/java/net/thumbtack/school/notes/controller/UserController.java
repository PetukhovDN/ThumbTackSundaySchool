package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.request.user.UpdateUserInfoRequest;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/api")
public class UserController {
    final UserServiceImpl userService;
    final String JAVASESSIONID = "JAVASESSIONID";

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping(value = "accounts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse registerUser(@RequestBody @Valid RegisterRequest registerRequest,
                                         HttpServletResponse response) throws NoteServerException {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(JAVASESSIONID, sessionId);
        response.addCookie(cookie);
        return UserMapStruct.INSTANCE.responseRegisterUser(userService.registerUser(registerRequest, sessionId));
    }

    @PostMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody @Valid LoginRequest loginRequest,
                          HttpServletResponse response) throws NoteServerException {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(JAVASESSIONID, sessionId);
        response.addCookie(cookie);
        userService.loginUser(loginRequest, sessionId);

    }

    @DeleteMapping(value = "sessions")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(@CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.logoutUser(sessionId);
    }

    @GetMapping(value = "account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getUserInfo(@CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {

        return UserMapStruct.INSTANCE.responseRegisterUser(userService.getUserInfo(sessionId));
    }

    @DeleteMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void leaveServer(@RequestBody @Valid LeaveServerRequest leaveRequest,
                            @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.leaveServer(leaveRequest, sessionId);
    }

    @PutMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserInfoResponse updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest updateRequest,
                                                 @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return userService.updateUserInfo(updateRequest, sessionId);
    }

    @PutMapping(value = "accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void makeAdmin(@PathVariable("id") int id,
                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.makeAdmin(id, sessionId);
    }


}

