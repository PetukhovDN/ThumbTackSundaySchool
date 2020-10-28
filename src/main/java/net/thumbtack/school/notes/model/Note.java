package net.thumbtack.school.notes.model;

import java.util.Date;
import java.util.Objects;

public class Note {

    private int id;
    private String noteHead;
    private String noteBody;
    private int revision;
    private Date creationTime;
    private User author;
    private Section section;

    public Note(String noteHead, String noteBody, Section section) {
        this.noteHead = noteHead;
        this.noteBody = noteBody;
        this.revision = 1;
        this.creationTime = new Date();
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteHead() {
        return noteHead;
    }

    public void setNoteHead(String noteHead) {
        this.noteHead = noteHead;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        Note note = (Note) o;
        return Objects.equals(getNoteHead(), note.getNoteHead()) &&
                Objects.equals(getSection(), note.getSection());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNoteHead(), getSection());
    }
}
