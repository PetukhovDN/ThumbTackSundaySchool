package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.Note;

public interface NoteDao {
    Note createNote(Note note, int sectionId);

    Note getNoteInfo(int noteId);

    Note changeNote(int noteId, String noteBody);

    Note replaceNote(int noteId, int sectionId);

    void deleteNote(int noteId);

    void deleteAllCommentsForNote(int noteId);

    void rateNote(int noteId, int rating);
}
