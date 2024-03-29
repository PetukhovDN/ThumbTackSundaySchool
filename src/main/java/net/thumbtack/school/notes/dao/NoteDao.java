package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;

public interface NoteDao {
    Integer createNote(Note note) throws NoteServerException;

    String createNoteRevision(NoteRevision noteRevision);

    Note getNoteInfo(int noteId) throws NoteServerException;

    NoteRevision getNoteRevisionInfo(String revisionId, int noteId) throws NoteServerException;

    void updateNoteLastRevision(int noteId, String revisionId);

    void replaceNoteToOtherSection(int noteId, int sectionId);

    void deleteNote(int noteId);

    void rateNote(int noteId, int userId, int rating);
}
