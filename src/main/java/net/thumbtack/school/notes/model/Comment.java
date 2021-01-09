package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Class describing comment model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    /**
     * Comment identifier
     */
    int id;

    /**
     * Comment text
     */
    String commentBody;

    /**
     * Comment author
     */
    User author;

    /**
     * Note to which comment belongs
     */
    Note note;

    /**
     * Note revision to which comment belongs
     */
    NoteRevision noteRevision;

    /**
     * Comment time of creation
     */
    LocalDateTime creationTime;
}
