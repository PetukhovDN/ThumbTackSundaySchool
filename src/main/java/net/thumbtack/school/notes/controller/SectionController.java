package net.thumbtack.school.notes.controller;

import net.thumbtack.school.notes.service.impl.SectionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sections")
public class SectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionController.class);

    private final SectionServiceImpl sectionService;

    @Autowired
    public SectionController(SectionServiceImpl sectionService) {
        this.sectionService = sectionService;
    }
}
