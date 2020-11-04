package net.thumbtack.school.notes.service.impl;

import net.thumbtack.school.notes.dao.impl.NoteDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private NoteDaoImpl noteDao;

    @Autowired
    public NoteServiceImpl(NoteDaoImpl noteDao) {
        this.noteDao = noteDao;
    }
}
