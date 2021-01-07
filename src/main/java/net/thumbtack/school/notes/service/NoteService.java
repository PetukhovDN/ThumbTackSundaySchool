package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.note.EditRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.springframework.transaction.annotation.Transactional;

public interface NoteService {
    @Transactional
    NoteResponse createNote(NoteRequest createRequest, String sessionId) throws NoteServerException;

    @Transactional
    NoteResponse getNoteInfo(int noteId, String sessionId) throws NoteServerException;

    @Transactional
    NoteResponse editNote(EditRequest editRequest, int noteId, String sessionId) throws NoteServerException;

    @Transactional
    void deleteNote(int noteId, String sessionId) throws NoteServerException;
}
