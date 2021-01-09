package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.dto.response.section.SectionResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SectionService {
    @Transactional
    SectionResponse createSection(SectionRequest createRequest, String sessionId) throws NoteServerException;

    @Transactional
    SectionResponse renameSection(SectionRequest createRequest, String sessionId, int sectionId) throws NoteServerException;

    @Transactional
    void deleteSection(String sessionId, int sectionId) throws NoteServerException;

    @Transactional
    SectionResponse getSectionInfo(String sessionId, int sectionId) throws NoteServerException;

    @Transactional
    List<SectionResponse> getAllSections(String sessionId) throws NoteServerException;
}
