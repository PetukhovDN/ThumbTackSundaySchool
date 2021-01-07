package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    LocalDateTime lastAccessTime;
    int expiryTime;
}
