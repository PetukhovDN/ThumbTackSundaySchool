package net.thumbtack.school.notes.service.impl;

import net.thumbtack.school.notes.dao.impl.CommentDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private CommentDaoImpl commentDao;

    @Autowired
    public CommentServiceImpl(CommentDaoImpl commentDao) {
        this.commentDao = commentDao;
    }
}
