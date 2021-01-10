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
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
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
        return userService.updateUserInfo(updateRequest, sessionId);
    }

    @PutMapping(value = "accounts/{id}/super")
    @ResponseStatus(HttpStatus.OK)
    public void makeAdmin(@PathVariable(value = "id") String id,
                          @CookieValue(name = JAVASESSIONID, required = false) String sessionId) throws NoteServerException {
        try {
            int userId = Integer.parseInt(id);
            userService.makeAdmin(userId, sessionId);
        } catch (NumberFormatException ex) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_USER_IDENTIFIER, id);
        }
    }

    @GetMapping(value = "accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UsersInfoResponse> getUsersInfoWithParams(@CookieValue(name = JAVASESSIONID, required = false) String sessionId,
                                                          @RequestParam(value = "sortByRating", required = false) ParamSort paramSort,
                                                          @RequestParam(value = "type ", required = false) ParamType paramType,
                                                          @RequestParam(value = "from", required = false) String from,
                                                          @RequestParam(value = "count", required = false) String count) throws NoteServerException {
        List<User> userAccountsInfo;
        if (paramType == null) {
            userAccountsInfo = userService.getAllUsers(sessionId);
        } else {
            userAccountsInfo = userService.getAllUsersByType(paramType, sessionId);
        }
        if (paramSort != null) {
            userAccountsInfo = userService.sortByRating(paramSort, userAccountsInfo);
        }
        try {
            if (from != null && !from.isEmpty()) {
                userAccountsInfo.removeAll(userAccountsInfo.subList(0, Integer.parseInt(from)));
            }
        } catch (NumberFormatException exception) {
            log.error("From must be a number");
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_FROM_FORMAT, from);
        }
        try {
            if (count != null && !count.isEmpty()) {
                userAccountsInfo.removeAll(userAccountsInfo.subList(Integer.parseInt(count) + 1, userAccountsInfo.size() - 1));
            }
        } catch (NumberFormatException exc) {
            log.error("Count must be a number");
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_COUNT_FORMAT, count);
        }
        ArrayList<UsersInfoResponse> usersResponse = new ArrayList<>();
        for (User user : userAccountsInfo) {
            UsersInfoResponse response = UserMapStruct.INSTANCE.responseGetAllUsers(user);
            usersResponse.add(response);
        }
        return usersResponse;
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

