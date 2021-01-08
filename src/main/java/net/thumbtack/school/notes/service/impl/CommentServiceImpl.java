package net.thumbtack.school.notes.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.CommentDaoImpl;
import net.thumbtack.school.notes.dao.impl.SessionDaoImpl;
import net.thumbtack.school.notes.dto.mappers.CommentMapStruct;
import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.request.comment.EditCommentRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Session;
import net.thumbtack.school.notes.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class CommentServiceImpl implements CommentService {
    CommentDaoImpl commentDao;
    SessionDaoImpl sessionDao;

    @Override
    @Transactional
    public CommentResponse addComment(CommentRequest addRequest, String sessionId) throws NoteServerException {
        log.info("Trying to add new comment to note with id {} ", addRequest.getNoteId());
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        Comment comment = CommentMapStruct.INSTANCE.requestAddComment(addRequest);
        CommentResponse response = CommentMapStruct.INSTANCE.responseAddComment(comment);
        sessionDao.updateSession(userSession);
        return response;
    }

    @Override
    @Transactional
    public List<CommentResponse> getAllNoteComments(int noteId, String sessionId) throws NoteServerException {
        log.info("Trying to get all comments for note with id {} ", noteId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        List<Comment> allNoteComments = commentDao.getAllCommentsForNote(noteId);
        List<CommentResponse> responses = new ArrayList<>();
        for (Comment allNoteComment : allNoteComments) {
            responses.add(CommentMapStruct.INSTANCE.responseAddComment(allNoteComment));
        }
        sessionDao.updateSession(userSession);
        return responses;
    }

    @Override
    @Transactional
    public CommentResponse editComment(EditCommentRequest editRequest, int commentId, String sessionId) throws NoteServerException {
        log.info("Trying to edit comment with id {} ", commentId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        int authorId = commentDao.getCommentInfo(commentId).getAuthor().getId();
        if (authorId != currentUserId) {
            throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_COMMENT, "You are not creator of this comment");
        }
        Comment comment = commentDao.changeComment(commentId, editRequest.getBody());
        CommentResponse response = CommentMapStruct.INSTANCE.responseAddComment(comment);
        sessionDao.updateSession(userSession);
        return response;
    }

    @Override
    @Transactional
    public void deleteComment(int commentId, String sessionId) throws NoteServerException {
        log.info("Trying to delete comment with id {} ", commentId);
        Session userSession = sessionDao.getSessionBySessionId(sessionId);
        int currentUserId = userSession.getUserId();
        Comment comment = commentDao.getCommentInfo(commentId);
        int noteAuthor = comment.getNote().getAuthor().getId();
        if (currentUserId != noteAuthor) {
            int commentAuthor = comment.getAuthor().getId();
            if (commentAuthor != currentUserId) {
                throw new NoteServerException(ExceptionErrorInfo.NOT_AUTHOR_OF_COMMENT, "You are not creator of this comment");
            }
        }
        commentDao.deleteComment(commentId);
        sessionDao.updateSession(userSession);
    }
}
