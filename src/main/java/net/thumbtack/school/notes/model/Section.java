package net.thumbtack.school.notes.model;

import java.util.Date;
import java.util.Objects;

public class Section {

    int id;
    private String name;
    private User author;
    private Date creationTime;

    public Section(String name) {
        this.name = name;
        this.creationTime = new Date();
    }

    public int getId() {
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
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
