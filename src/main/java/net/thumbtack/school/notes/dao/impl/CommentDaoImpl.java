package net.thumbtack.school.notes.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.CommentDao;
import net.thumbtack.school.notes.mappers.CommentMapper;
import net.thumbtack.school.notes.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentDaoImpl implements CommentDao {
    private final CommentMapper commentMapper;

    @Override
    public Comment createComment(Comment comment) {
        return null;
    }

    @Override
    public List<Comment> getAllCommentsForNote(int noteId) {
        return null;
    }

    @Override
    public Comment changeComment(int commentId, String newCommentBody) {
        return null;
    }

    @Override
    public void deleteComment(int commentId) {

    }
}
