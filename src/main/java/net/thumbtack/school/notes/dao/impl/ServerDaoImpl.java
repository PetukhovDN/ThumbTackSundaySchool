package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.ServerDao;
import net.thumbtack.school.notes.mappers.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ServerDaoImpl implements ServerDao {
    private final UserMapper userMapper;
    private final NoteMapper noteMapper;
    private final SectionMapper sectionMapper;
    private final CommentMapper commentMapper;
    private final SessionMapper sessionMapper;


    @Override
    public void clear() {
        log.info("Trying to clear database");
        try {
            userMapper.deleteAll();
            noteMapper.deleteAll();
            sectionMapper.deleteAll();
            commentMapper.deleteAll();
            sessionMapper.deleteAll();
        } catch (RuntimeException ex) {
            log.error("Can't clear database");
            throw ex;
        }
        log.info("Database was cleared");
    }
}
