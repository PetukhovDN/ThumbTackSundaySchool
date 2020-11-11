package net.thumbtack.school.notes.service.impl;

import net.thumbtack.school.notes.dao.impl.ServerDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private final ServerDaoImpl serverDao;

    @Autowired
    public DebugService(ServerDaoImpl serverDao) {
        this.serverDao = serverDao;
    }

    public void clearDatabase() {
        serverDao.clear();
    }
}
