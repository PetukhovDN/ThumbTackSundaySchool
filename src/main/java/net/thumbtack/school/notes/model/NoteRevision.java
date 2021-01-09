package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class describing note revision model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NoteRevision {

    /**
     * Note revision identifier
     */
    String revisionId;

    /**
     * Note body in text (new for every new note revision)
     */
    String body;

    /**
     * Identifier of the note that current revision belongs to
     */
    int noteId;

    /**
     * Note revision time of creation
     */
    LocalDateTime creationTime;

    /**
     * List of all comments that belong to the current note revision
     */
    List<Comment> comments;
}
