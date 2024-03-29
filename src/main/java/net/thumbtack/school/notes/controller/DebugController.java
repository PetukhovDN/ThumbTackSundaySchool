package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.response.user.ServerSettingsResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.impl.DebugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/api/debug")
public class DebugController {
    DebugService debugService;

    @PostMapping(value = "clear")
    @ResponseStatus(HttpStatus.OK)
    public void clearDatabase() {
        debugService.clearDatabase();
    }

    @GetMapping(value = "settings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ServerSettingsResponse getServerSettings() {
        return debugService.getServerSettings();
    }

    @PostMapping(value = "super")
    @ResponseStatus(HttpStatus.OK)
    public void makeSuper(HttpServletResponse response) throws NoteServerException {
        User user = debugService.registerUser();
        User admin = debugService.makeAdmin(user);
        String sessionId = debugService.loginUser(admin.getId());
        Cookie cookie = new Cookie("JAVASESSIONID", sessionId);
        response.addCookie(cookie);
    }

    @GetMapping(value = "{login}/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUserIdByLogin(@PathVariable(value = "login") String login) throws NoteServerException {
        return debugService.getUserAccountInfoByLogin(login);
    }
}
