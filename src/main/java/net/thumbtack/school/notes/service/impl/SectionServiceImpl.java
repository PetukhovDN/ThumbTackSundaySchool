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
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.enums.UserStatus;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.service.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to work with sections of notes on the server
 * In every method check`s if session is alive and updates session life time after successful request
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class SectionServiceImpl implements SectionService {
    SectionDaoImpl sectionDao;
    SessionDaoImpl sessionDao;
    UserDaoImpl userDao;

    /**
     * Method to create new note`s section on the server
     *
     * @param createRequest contains name of new section
     * @param sessionId     user session token
     * @return created section information
     */
    @Override
    @Transactional
    public SectionResponse createSection(SectionRequest createRequest, String sessionId) throws NoteServerException {
        log.info("Trying to create new section");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        User author = userDao.getUserById(userSession.getUserId());
        Section section = SectionMupStruct.INSTANCE.requestCreateSection(createRequest);
        section.setAuthor(author);
        int resultSectionId = sectionDao.createSection(section, author.getId());
        Section resultSection = sectionDao.getSectionInfo(resultSectionId);
        SectionResponse response = SectionMupStruct.INSTANCE.responseCreateSection(resultSection);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to rename existing section name
     *
     * @param renameRequest contains new section name
     * @param sessionId     current user session token
     * @param sectionId     identifier of section to be renamed
     * @return created section information
     * @throws NoteServerException if owner of current session is not the author of section with given identifier
     */
    @Override
    @Transactional
    public SectionResponse renameSection(SectionRequest renameRequest, String sessionId, int sectionId) throws NoteServerException {
        log.info("Trying to rename existed section");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        Section section = sectionDao.getSectionInfo(sectionId);
        if (section.getAuthor().getId() != userSession.getUserId()) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
        }
        int resultSectionId = sectionDao.renameSection(sectionId, section.getSectionName());
        Section resultSection = sectionDao.getSectionInfo(resultSectionId);
        SectionResponse response = SectionMupStruct.INSTANCE.responseCreateSection(resultSection);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to delete note`s section from the server
     * User can only delete section whose author he is
     * Admin can delete any section
     *
     * @param sessionId current user session token
     * @param sectionId identifier of section to be deleted
     * @throws NoteServerException if owner of current session is not the author of section with given identifier
     */
    @Override
    @Transactional
    public void deleteSection(String sessionId, int sectionId) throws NoteServerException {
        log.info("Trying to delete section");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        User user = userDao.getUserById(userSession.getUserId());
        if (!user.getUserStatus().equals(UserStatus.ADMIN)) {
            Section section = sectionDao.getSectionInfo(sectionId);
            if (section.getAuthor().getId() != userSession.getUserId()) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_SECTION, "You are not creator of this section");
            }
        }
        sectionDao.deleteSection(sectionId);
        sessionDao.updateSession(userSession);
    }

    /**
     * Method to get existing section information
     *
     * @param sessionId current user session token
     * @param sectionId identifier of section for which information is needed
     * @return information about section with given identifier
     */
    @Override
    @Transactional
    public SectionResponse getSectionInfo(String sessionId, int sectionId) throws NoteServerException {
        log.info("Trying to get information about section");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        Section resultSection = sectionDao.getSectionInfo(sectionId);
        SectionResponse response = SectionMupStruct.INSTANCE.responseCreateSection(resultSection);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to get information about all sections on the server
     *
     * @param sessionId current user session token
     * @return information about all existing sections on the server in list
     */
    @Override
    @Transactional
    public List<SectionResponse> getAllSections(String sessionId) throws NoteServerException {
        log.info("Trying to get information about all sections");
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        List<Section> sections = sectionDao.getAllSections();
        List<SectionResponse> responses = new ArrayList<>();
        for (Section section : sections) {
            responses.add(SectionMupStruct.INSTANCE.responseCreateSection(section));
        }
        sessionDao.updateSession(userSession);
        return responses;
    }
}
