package net.thumbtack.school.notes.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Class describing note`s section model
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    /**
     * Section identifier
     */
    int id;

    /**
     * Section name
     */
    String sectionName;

    /**
     * User who created current section
     */
    User author;

    /**
     * Section time of creation
     */
    LocalDateTime creationTime;
}
