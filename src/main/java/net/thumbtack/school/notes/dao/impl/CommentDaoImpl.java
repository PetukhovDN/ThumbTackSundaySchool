package net.thumbtack.school.notes.dao.impl;

import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentDaoImpl extends DaoImplBase implements CommentDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentDaoImpl.class);


    @Override
    public Comment createComment(String token, Comment comment) {
        return null;
    }

    @Override
    public List<Comment> getAllCommentsForNote(String token, int noteId) {
        return null;
    }

    @Override
    public Comment changeComment(String token, int commentId, String newCommentBody) {
        return null;
    }

    @Override
    public void deleteComment(String token, int commentId) {

    }
}
