package net.thumbtack.school.notes.dto.response.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServerSettingsResponse {
    String maxNameLength;
    String minPasswordLength;
    String userIdleTimeout;
}
