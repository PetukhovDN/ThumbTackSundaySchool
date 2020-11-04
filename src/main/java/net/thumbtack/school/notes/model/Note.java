package net.thumbtack.school.notes.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Note {

    private int id;
    private String noteHead;
    private String noteBody;
    private User author;
    private Section section;
    private LocalDateTime creationTime;

    public Note() {
    }

    public Note(String noteHead, String noteBody) {
        this.noteHead = noteHead;
        this.noteBody = noteBody;
    }

    public Note(int id,
                String noteHead,
                String noteBody,
                User author,
                Section section,
                LocalDateTime creationTime) {
        this.id = id;
        this.noteHead = noteHead;
        this.noteBody = noteBody;
        this.author = author;
        this.section = section;
        this.creationTime = creationTime;
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
