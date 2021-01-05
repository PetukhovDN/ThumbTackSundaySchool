package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.section.SectionRequest;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SectionService {
    @Transactional
    Section createSection(SectionRequest createRequest, String sessionId) throws NoteServerException;

    @Transactional
    Section renameSection(SectionRequest createRequest, String sessionId, long sectionId) throws NoteServerException;

    @Transactional
    void deleteSection(String sessionId, long sectionId) throws NoteServerException;

    @Transactional
    Section getSectionInfo(String sessionId, long sectionId) throws NoteServerException;

    @Transactional
    List<Section> getAllSections(String sessionId) throws NoteServerException;
}
