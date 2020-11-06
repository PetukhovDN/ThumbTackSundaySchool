package net.thumbtack.school.notes.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Note {

    private int id;
    private String noteHead;
    private Map<Integer, String> noteBodyWithRevision;
    private User author;
    private Section section;
    private LocalDateTime creationTime;

    public Note() {
    }

    public Note(String noteHead, Map<Integer, String> noteBodyWithRevision) {
        this.noteHead = noteHead;
        this.noteBodyWithRevision = noteBodyWithRevision;
    }

    public Note(int id,
                String noteHead,
                Map<Integer, String> noteBodyWithRevision,
                User author,
                Section section,
                LocalDateTime creationTime) {
        this.id = id;
        this.noteHead = noteHead;
        this.noteBodyWithRevision = noteBodyWithRevision;
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

    public Map<Integer, String> getNoteBody() {
        return noteBodyWithRevision;
    }

    public void setNoteBody(Map<Integer, String> noteBodyWithRevision) {
        this.noteBodyWithRevision = noteBodyWithRevision;
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
