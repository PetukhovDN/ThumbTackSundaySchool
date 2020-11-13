package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.NoteDaoImpl;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {
    private final NoteDaoImpl noteDao;

}
