package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.ServerDaoImpl;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DebugService {
    private final ServerDaoImpl serverDao;

    public void clearDatabase() {
        serverDao.clear();
    }

    public String makeAdmin(User user) {
        return serverDao.makeAdmin(user);
    }

    public User getUserIdByLogin(String login) throws NoteServerException {
        return serverDao.getUserByLogin(login);
    }
}
