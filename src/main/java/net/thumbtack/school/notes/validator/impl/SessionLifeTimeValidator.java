package net.thumbtack.school.notes.validator.impl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.validator.SessionLifeTime;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneId;

//TODO: start to use instead of method int session dao impl

/**
 * Session life time validation
 */
@Slf4j
public class SessionLifeTimeValidator implements ConstraintValidator<SessionLifeTime, LocalDateTime> {
    /**
     * Time for which user session is alive
     * Set in application.properties
     */
    @Value("${user_idle_timeout}")
    int sessionLifeTime;

    /**
     * The time elapsed since the last action in session must be less than session life time (in seconds)
     * Session life time is set in application.properties (value user_idle_timeout)
     */
    @Override
    public boolean isValid(LocalDateTime lastAccessTime, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Checking if session is valid");
        long sessionStartTimeInSec = lastAccessTime
                .atZone(ZoneId.of("Asia/Omsk"))
                .toInstant()
                .toEpochMilli() / 1000;
        long currentTimeInSec = LocalDateTime.now().atZone(ZoneId.of("Asia/Omsk")).toInstant().toEpochMilli() / 1000;
        return currentTimeInSec < sessionStartTimeInSec + sessionLifeTime;
    }
}
