package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Class describing user session model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    /**
     * Session identifier
     */
    String sessionId;

    /**
     * Identifier of the user that session belongs to
     */
    int userId;

    /**
     * Session time of creation
     */
    LocalDateTime creationTime;

    /**
     * Session last access time
     */
    LocalDateTime lastAccessTime;

    /**
     * Session life time
     */
    int expiryTime;
}
