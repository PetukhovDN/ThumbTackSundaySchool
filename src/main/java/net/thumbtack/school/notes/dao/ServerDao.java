package net.thumbtack.school.notes.dao;

import net.thumbtack.school.notes.model.User;

public interface ServerDao {
    void clear();

    void makeAdmin(User user);
}
