package net.thumbtack.school.notes.service.impl;

import net.thumbtack.school.notes.dao.impl.SectionDaoImpl;
import net.thumbtack.school.notes.dao.impl.UserDaoImpl;
import net.thumbtack.school.notes.service.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionServiceImpl implements SectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private SectionDaoImpl sectionDao;

    @Autowired
    public SectionServiceImpl(SectionDaoImpl sectionDao) {
        this.sectionDao = sectionDao;
    }
}
