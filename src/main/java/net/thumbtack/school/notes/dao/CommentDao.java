package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.Comment;

import java.util.List;

public interface CommentDao {
    Comment createComment(Comment comment);

    List<Comment> getAllCommentsForNote(int noteId);

    Comment changeComment(int commentId, String newCommentBody);

    void deleteComment(int commentId);
}
