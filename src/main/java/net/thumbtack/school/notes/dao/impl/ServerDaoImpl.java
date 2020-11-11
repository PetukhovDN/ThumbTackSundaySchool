package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.ServerDao;
import net.thumbtack.school.notes.mappers.CommentMapper;
import net.thumbtack.school.notes.mappers.NoteMapper;
import net.thumbtack.school.notes.mappers.SectionMapper;
import net.thumbtack.school.notes.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerDaoImpl implements ServerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerDaoImpl.class);

    private final UserMapper userMapper;
    private final NoteMapper noteMapper;
    private final SectionMapper sectionMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ServerDaoImpl(UserMapper userMapper,
                         NoteMapper noteMapper,
                         SectionMapper sectionMapper,
                         CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
        this.sectionMapper = sectionMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public void clear() {
        LOGGER.info("Trying to clear database");
        try {
            userMapper.deleteAll();
            noteMapper.deleteAll();
            sectionMapper.deleteAll();
            commentMapper.deleteAll();
        } catch (RuntimeException ex) {
            LOGGER.error("Can't clear database");
            throw ex;
        }
        LOGGER.info("Database was cleared");
    }
}
