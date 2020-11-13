package net.thumbtack.school.notes;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan("net.thumbtack.school.notes.mappers")
public class NotesServer {
    public static void main(String[] args) {
        log.info("Starting notes server");
        SpringApplication.run(NotesServer.class, args);
        log.info("Server has started");
    }
}
