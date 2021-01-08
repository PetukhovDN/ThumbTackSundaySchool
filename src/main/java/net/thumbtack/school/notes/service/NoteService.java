package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.comment.RateCommentRequest;
import net.thumbtack.school.notes.dto.request.note.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.note.NoteRequest;
import net.thumbtack.school.notes.dto.response.note.NoteResponse;
import net.thumbtack.school.notes.dto.response.note.NotesInfoResponseWithParams;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.params.NoteRequestParam;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface NoteService {
    @Transactional
    NoteResponse createNote(NoteRequest createRequest, String sessionId) throws NoteServerException;

    @Transactional
    NoteResponse getNoteInfo(int noteId, String sessionId) throws NoteServerException;

    @Transactional
    NoteResponse editNote(EditNoteRequest editNoteRequest, int noteId, String sessionId) throws NoteServerException;

    @Transactional
    void deleteNote(int noteId, String sessionId) throws NoteServerException;

    @Transactional
    void deleteAllNoteComments(int noteId, String sessionId) throws NoteServerException;

    @Transactional
    void rateNote(RateCommentRequest rateRequest, int noteId, String sessionId) throws NoteServerException;

    @Transactional
    List<NotesInfoResponseWithParams> getNotesInfo(NoteRequestParam requestParam, @NotNull String sessionId) throws NoteServerException;
}
