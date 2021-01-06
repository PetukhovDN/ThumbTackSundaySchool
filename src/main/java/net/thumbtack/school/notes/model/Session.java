package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.validator.SessionLifeTime;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    int id;
    String sessionId;
    int userId;
    LocalDateTime creationTime;
    @SessionLifeTime
    LocalDateTime lastAccessTime;
    int expiryTime;
}
