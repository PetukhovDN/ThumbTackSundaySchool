package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.service.impl.DebugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/debug")
public class DebugController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugController.class);

    private DebugService debugService;

    @Autowired

    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @PostMapping(value = "clear", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void clearDatabase() {
        debugService.clearDatabase();
    }
}
