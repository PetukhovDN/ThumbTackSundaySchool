package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/ignore")
public class IgnoreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreController.class);

    private final UserServiceImpl userService;

    @Autowired
    public IgnoreController(UserServiceImpl userService) {
        this.userService = userService;
    }
}
