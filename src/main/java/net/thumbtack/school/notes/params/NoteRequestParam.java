package net.thumbtack.school.notes.params;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.ParamInclude;
import net.thumbtack.school.notes.enums.ParamSort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User request parameters for getting list of notes from the server
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestParam {

    /**
     * Parameter that selects from what section get notes from the server
     */
    int sectionId;

    /**
     * Parameter that selects how to sort notes info
     */
    ParamSort sortByRating;

    /**
     * Keywords for finding notes
     */
    List<String> tags;

    /**
     * If true uses all keywords from list tags, else at least one keyword
     */
    boolean allTags;

    /**
     * Parameter that selects notes, made after the given time
     */
    LocalDateTime timeFrom;

    /**
     * Parameter that selects notes, made before the given time
     */
    LocalDateTime timeTo;

    /**
     * Parameter that selects notes, made by user with given identifier
     */
    int userId;

    /**
     * Specifies which notes should be issued
     * If the "user" parameter is specified, the include parameter is ignored.
     */
    ParamInclude include;

    /**
     * Parameter that selects notes with all there comments
     */
    boolean comments;

    /**
     * Parameter that selects notes with all there revisions
     */
    boolean allVersions;

    /**
     * For each comment is given revision of the note to which this comment was made
     */
    boolean commentVersion;

    /**
     * Parameter that selects from what position get notes from server
     */
    int from;

    /**
     * Parameter that selects how many notes get from server
     */
    int count;
}
