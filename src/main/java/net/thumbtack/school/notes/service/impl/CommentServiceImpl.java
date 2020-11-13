package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.CommentDaoImpl;
import net.thumbtack.school.notes.service.CommentService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDaoImpl commentDao;

}
