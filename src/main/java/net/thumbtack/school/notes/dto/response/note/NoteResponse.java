package net.thumbtack.school.notes.dto.response.note;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteResponse {
    int id;
    @NotNull
    String subject;
    @NotNull
    String body;
    int sectionId;
    int authorId;
    LocalDateTime created;
    String revisionId;
}
