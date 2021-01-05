package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Section;

import java.util.List;

public interface SectionDao {
    Section createSection(Section section) throws NoteServerException;

    Section renameSection(long sectionId, String newSectionName);

    void deleteSection(long sectionId);

    Section getSectionInfo(long sectionId) throws NoteServerException;

    List<Section> getAllSections();
}
