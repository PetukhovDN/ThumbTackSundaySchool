package net.thumbtack.school.notes.model;

import java.util.Date;

public class Comment {

    private int id;
    private String commentText;
    private Date creationTime;
    private User author;
    private Note note;

    public Comment(String commentText, Note note) {
        this.commentText = commentText;
        this.creationTime = new Date();
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
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
}
