package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.SectionDaoImpl;
import net.thumbtack.school.notes.dao.impl.SessionDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.dto.mappers.SectionMupStruct;
import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class SectionServiceImpl implements SectionService {
    SectionDaoImpl sectionDao;
    SessionDaoImpl sessionDao;
    UserDaoImpl userDao;

    @Override
    @Transactional
    public Section createSection(SectionRequest createRequest, String sessionId) throws NoteServerException {
        log.info("Trying to create new section");
        int userId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        Section section = SectionMupStruct.INSTANCE.requestCreateSection(createRequest);
        section.setAuthorId(userId);
        return sectionDao.createSection(section);
    }

    @Override
    @Transactional
    public Section renameSection(SectionRequest renameRequest, String sessionId, long sectionId) throws NoteServerException {
        log.info("Trying to rename existed section");
        int userId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        Section section = sectionDao.getSectionInfo(sectionId);
        if (section.getAuthorId() != userId) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
        }
        return sectionDao.renameSection(sectionId, section.getSectionName());
    }

    @Override
    @Transactional
    public void deleteSection(String sessionId, long sectionId) throws NoteServerException {
        log.info("Trying to delete section");
        int userId = sessionDao.getSessionBySessionId(sessionId).getUserId();
        Section section = sectionDao.getSectionInfo(sectionId);
        User user = userDao.getUserById(userId);
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            if (section.getAuthorId() != userId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
            }
        }
        sectionDao.deleteSection(sectionId);
    }

    @Override
    @Transactional
    public Section getSectionInfo(String sessionId, long sectionId) throws NoteServerException {
        log.info("Trying to get information about section");
        sessionDao.getSessionBySessionId(sessionId);
        return sectionDao.getSectionInfo(sectionId);
    }

    @Override
    @Transactional
    public List<Section> getAllSections(String sessionId) throws NoteServerException {
        log.info("Trying to get information about all sections");
        sessionDao.getSessionBySessionId(sessionId);
        return sectionDao.getAllSections();
    }


}
