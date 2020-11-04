package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/debug")
public class ServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

}
