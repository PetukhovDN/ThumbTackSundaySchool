package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.mappers.UserMapStruct;
import net.thumbtack.school.notes.dto.request.user.*;
import net.thumbtack.school.notes.dto.response.user.UpdateUserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.dto.response.user.UsersInfoResponse;
import net.thumbtack.school.notes.enums.ParamSort;
import net.thumbtack.school.notes.enums.ParamType;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.params.UserRequestParam;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
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
        String newSessionId = UUID.randomUUID().toString();
        response.addCookie(new Cookie(JAVASESSIONID, newSessionId));
        return UserMapStruct.INSTANCE.responseRegisterUser(userService.registerUser(registerRequest, newSessionId));
    }

    @PostMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody LoginRequest loginRequest,
                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId,
                          HttpServletResponse response) throws NoteServerException {
        String newSessionId = UUID.randomUUID().toString();
        userService.loginUser(loginRequest, sessionId, newSessionId);
        response.addCookie(new Cookie(JAVASESSIONID, newSessionId));
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
    public void leaveServer(@RequestBody LeaveServerRequest leaveRequest,
                            @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.leaveServer(leaveRequest, sessionId);
    }

    @PutMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserInfoResponse updateUserInfo(@RequestBody UpdateUserInfoRequest updateRequest,
                                                 @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        return UserMapStruct.INSTANCE.responseUpdateUserInfo(userService.updateUserInfo(updateRequest, sessionId));
    }

    @PutMapping(value = "accounts/{id}/super")
    @ResponseStatus(HttpStatus.OK)
    public void makeAdmin(@PathVariable(value = "id") int id,
                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.makeAdmin(id, sessionId);
    }


    @GetMapping(value = "accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UsersInfoResponse> getAllUsersInfoWithParams(@CookieValue(name = JAVASESSIONID, required = false) String sessionId,
                                                             @RequestParam(value = "sortByRating", required = false) ParamSort paramSort,
                                                             @RequestParam(value = "type ", required = false) ParamType paramType,
                                                             @RequestParam(value = "from", required = false) String from,
                                                             @RequestParam(value = "count", required = false) String count) throws NoteServerException {
        return userService.getAllUsersInfo(new UserRequestParam(paramSort, paramType, from, count), sessionId);
    }

    @PostMapping(value = "followings",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void followUser(@RequestBody FollowIgnoreRequest request,
                           @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.followUser(request.getLogin(), sessionId);
    }

    @DeleteMapping(value = "followings/{login}")
    @ResponseStatus(HttpStatus.OK)
    public void stopFollowUser(@RequestParam(name = "login") String login,
                               @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.stopFollowUser(login, sessionId);
    }

    @PostMapping(value = "ignore",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void ignoreUser(@RequestBody FollowIgnoreRequest request,
                           @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.ignoreUser(request.getLogin(), sessionId);
    }

    @DeleteMapping(value = "ignore/{login}")
    @ResponseStatus(HttpStatus.OK)
    public void stopIgnoreUser(@RequestParam(name = "login") String login,
                               @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        userService.stopIgnoreUser(login, sessionId);
    }
}

