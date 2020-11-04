package net.thumbtack.school.notes;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("net.thumbtack.school.notes.mappers")
public class NotesServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotesServer.class);

    public static void main(String[] args) {
        LOGGER.info("Starting notes server");
        SpringApplication.run(NotesServer.class, args);
        LOGGER.info("Server has started");
    }
}
