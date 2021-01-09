package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class describing note model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    /**
     * Note identifier
     */
    int id;

    /**
     * Note subject in text
     */
    String subject;

    /**
     * Note last revision identifier
     */
    String lastRevisionId;

    /**
     * All revisions of current note
     */
    List<NoteRevision> revisions;

    /**
     * Author of note
     */
    User author;

    /**
     * Section where the note is located
     */
    Section section;

    /**
     * Note time of creation
     */
    LocalDateTime creationTime;

    /**
     * All ratings for the note
     */
    List<Integer> ratings;
}
