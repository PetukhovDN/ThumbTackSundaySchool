package net.thumbtack.school.notes.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {

    private int id;
    private String commentBody;
    private User author;
    private Note note;
    private LocalDateTime creationTime;

    public Comment() {
    }

    public Comment(String commentBody) {
        this.commentBody = commentBody;
    }

    public Comment(int id, String commentBody, User author, Note note, LocalDateTime creationTime) {
        this.id = id;
        this.commentBody = commentBody;
        this.author = author;
        this.note = note;
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(getCommentBody(), comment.getCommentBody()) &&
                Objects.equals(getAuthor(), comment.getAuthor()) &&
                Objects.equals(getNote(), comment.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentBody(), getAuthor(), getNote());
    }
}
