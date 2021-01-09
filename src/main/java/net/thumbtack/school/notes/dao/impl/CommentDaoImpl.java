package net.thumbtack.school.notes.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.mappers.CommentMapper;
import net.thumbtack.school.notes.model.Comment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataAccessObject to work with comments
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CommentDaoImpl implements CommentDao {
    CommentMapper commentMapper;

    /**
     * Method to save new comment to the database
     *
     * @param comment contains new comment information
     * @return created comment identifier in success
     * @throws NoteServerException if comment with such identifier already exists in database
     */
    @Override
    public Integer createComment(Comment comment) throws NoteServerException {
        log.info("DAO insert Comment {} to Database", comment);
        try {
            commentMapper.saveComment(comment);
            return comment.getId();
        } catch (DuplicateKeyException ex) {
            log.error("Comment {} already exists", comment, ex);
            throw new NoteServerException(ExceptionErrorInfo.COMMENT_ALREADY_EXISTS, "Comment with this id already exist");
        } catch (RuntimeException ex) {
            log.error("Can't insert Comment {} to Database, {}", comment, ex);
            throw ex;
        }
    }

    /**
     * Method to get all comments, that belongs to the note with given identifier, from database
     *
     * @param noteId note identifier
     * @return list, that contains information about all comments
     */
    @Override
    public List<Comment> getAllCommentsForNote(int noteId) {
        log.info("DAO get information about all Comments for note with id {} from Database", noteId);
        try {
            return commentMapper.getAllCommentsForNote(noteId);
        } catch (RuntimeException ex) {
            log.error("Can't get information about all Comments for note with id {} from Database, ", noteId, ex);
            throw ex;
        }
    }

    /**
     * Method to change comment body in database
     *
     * @param commentId      comment identifier
     * @param newCommentBody new comment information
     * @return identifier of changed comment in success
     */
    @Override
    public Integer changeComment(int commentId, String newCommentBody) {
        log.info("DAO change text for Comment with id {} in Database", commentId);
        try {
            commentMapper.updateCommentText(commentId, newCommentBody);
            return commentId;
        } catch (RuntimeException ex) {
            log.error("Can't change text for Comment with id {} in Database, {}", commentId, ex);
            throw ex;
        }
    }

    /**
     * Method to get comment information from database
     *
     * @param commentId comment identifier
     * @return comment information in success
     * @throws NoteServerException if there is no comment with such identifier in database
     */
    @Override
    public Comment getCommentInfo(int commentId) throws NoteServerException {
        log.info("DAO get information about comment with id {} from Database", commentId);
        try {
            Comment comment = commentMapper.getCommentById(commentId);
            if (comment == null) {
                log.error("No such note on the server");
                throw new NoteServerException(ExceptionErrorInfo.COMMENT_DOES_NOT_EXISTS, "No such comment on the server");
            }
            return comment;
        } catch (RuntimeException ex) {
            log.error("Can't get information about Comment with id {} from Database, {}", commentId, ex);
            throw ex;
        }
    }

    /**
     * Method to delete comment from database
     *
     * @param commentId comment identifier
     */
    @Override
    public void deleteComment(int commentId) {
        log.info("DAO delete Comment with id {} from Database", commentId);
        try {
            commentMapper.deleteCommentById(commentId);
        } catch (RuntimeException ex) {
            log.error("Can't delete Comment with id {} from Database, {}", commentId, ex);
            throw ex;
        }
    }

    /**
     * Method to delete all comments, that belongs to the note with given identifier
     *
     * @param noteId note identifier
     */
    @Override
    public void deleteAllNoteComments(int noteId) {
        log.info("DAO delete all Comments for Note with id {} from Database", noteId);
        try {
            commentMapper.deleteCommentsForNote(noteId);
        } catch (RuntimeException ex) {
            log.error("Can't delete all Comment for Note with id {} from Database, {}", noteId, ex);
            throw ex;
        }
    }
}
