package net.thumbtack.school.notes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dao.impl.SectionDaoImpl;
import net.thumbtack.school.notes.service.SectionService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {
    private final SectionDaoImpl sectionDao;

}
