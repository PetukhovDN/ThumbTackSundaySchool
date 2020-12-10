package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.ServerDao;
import net.thumbtack.school.notes.mappers.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class ServerDaoImpl implements ServerDao {
    UserMapper userMapper;
    NoteMapper noteMapper;
    SectionMapper sectionMapper;
    CommentMapper commentMapper;
    SessionMapper sessionMapper;


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
