package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/accounts")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

}

