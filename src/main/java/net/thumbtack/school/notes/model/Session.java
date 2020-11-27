package net.thumbtack.school.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private String sessionId;
    private User user;
    private long creationTime;
    private long lastAccessTime;
    private long expiryTime;
}
