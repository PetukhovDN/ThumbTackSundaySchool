package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/followings")
public class FollowingsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowingsController.class);

    private UserServiceImpl userService;

    @Autowired
    public FollowingsController(UserServiceImpl userService) {
        this.userService = userService;
    }
}
