package net.thumbtack.school.notes.dto.response.note;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.model.NoteRevision;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotesInfoResponseWithParams {
    int id;
    String subject;
    String body;
    int sectionId;
    int authorId;
    LocalDateTime created;
    List<NoteRevision> revisions;
}
