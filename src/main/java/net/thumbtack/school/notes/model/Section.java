package net.thumbtack.school.notes.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Section {

    private long id;
    private String name;
    private User author;
    private LocalDateTime creationTime;

    public Section() {
    }

    public Section(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Section(int id, String name, User author, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.creationTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(getName(), section.getName()) &&
                Objects.equals(getAuthor(), section.getAuthor()) &&
                Objects.equals(getCreationTime(), section.getCreationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAuthor(), getCreationTime());
    }
}
