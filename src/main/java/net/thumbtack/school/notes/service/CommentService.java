package net.thumbtack.school.notes.service;

import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {
    @Transactional
    CommentResponse addComment(CommentRequest addRequest, String sessionId) throws NoteServerException;

    @Transactional
    List<CommentResponse> getAllNoteComments(int noteId, String sessionId) throws NoteServerException;

    @Transactional
    CommentResponse editComment(EditCommentRequest editRequest, int commentId, String sessionId) throws NoteServerException;

    @Transactional
    void deleteComment(int commentId, String sessionId) throws NoteServerException;
}
