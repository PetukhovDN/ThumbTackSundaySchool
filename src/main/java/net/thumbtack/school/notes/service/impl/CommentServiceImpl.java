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

/**
 * Service to work with comments on the server
 * In every method check`s if session is alive and updates session life time after successful request
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class CommentServiceImpl implements CommentService {
    CommentDaoImpl commentDao;
    SessionDaoImpl sessionDao;

    /**
     * Method to add comment to existing note on the server
     * Response contains comment identifier, comment body, author identifier,
     * note identifier, note revision and time of creation
     *
     * @param addRequest contains comment body and identifier of existing note
     * @param sessionId  session token of current user
     * @return information about comment in success
     */
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

    /**
     * Method to get all comments belonging to the note with given identifier
     *
     * @param noteId    note identifier
     * @param sessionId session token of current user
     * @return list, which contains comment`s information
     */
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

    /**
     * Method to edit comment body
     *
     * @param editRequest contains new comment body
     * @param commentId   identifier of the comment to edit
     * @param sessionId   session token of current user
     * @return information about comment in success
     * @throws NoteServerException if current user is not the author of comment to edit
     */
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
        int editedCommentId = commentDao.changeComment(commentId, editRequest.getBody());
        Comment editedComment = commentDao.getCommentInfo(editedCommentId);
        CommentResponse response = CommentMapStruct.INSTANCE.responseAddComment(editedComment);
        sessionDao.updateSession(userSession);
        return response;
    }

    /**
     * Method to delete comment with given identifier
     *
     * @param commentId comment identifier
     * @param sessionId session token of current user
     * @throws NoteServerException if current user is not the author of comment to edit
     */
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
