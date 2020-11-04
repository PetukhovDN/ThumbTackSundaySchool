package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.Note;

public interface NoteDao {
    Note createNote(String token, Note note, int sectionId);

    Note getNoteInfo(String token, int noteId);

    Note changeNote(String token, int noteId, String noteBody);

    Note replaceNote(String token, int noteId, int sectionId);

    void deleteNote(String token, int noteId);

    void deleteAllCommentsForNote(String token, int noteId);

    void rateNote(String token, int noteId, int rating);
}
