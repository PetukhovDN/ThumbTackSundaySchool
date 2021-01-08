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

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CommentDaoImpl implements CommentDao {
    CommentMapper commentMapper;

    @Override
    public Comment createComment(Comment comment) throws NoteServerException {
        log.info("DAO insert Comment {} to Database", comment);
        try {
            commentMapper.saveComment(comment);
            return commentMapper.getCommentById(comment.getId());
        } catch (DuplicateKeyException ex) {
            log.error("Comment {} already exists", comment, ex);
            throw new NoteServerException(ExceptionErrorInfo.COMMENT_ALREADY_EXISTS, "Comment with this id already exist");
        } catch (RuntimeException ex) {
            log.error("Can't insert Comment {} to Database, {}", comment, ex);
            throw ex;
        }
    }

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

    @Override
    public Comment changeComment(int commentId, String newCommentBody) {
        log.info("DAO change text for Comment with id {} in Database", commentId);
        try {
            commentMapper.updateCommentText(commentId, newCommentBody);
            return commentMapper.getCommentById(commentId);
        } catch (RuntimeException ex) {
            log.error("Can't change text for Comment with id {} in Database, {}", commentId, ex);
            throw ex;
        }
    }

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
