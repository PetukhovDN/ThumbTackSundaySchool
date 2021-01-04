package net.thumbtack.school.notes.enums;

/**
 * Types of the returned list of notes after user request
 */
public enum ParamInclude {
    /**
     * Returns all notes, except notes from users, who are ignored by the owner of request
     */
    NOT_IGNORE,
    /**
     * Returns notes only from users, who are following the owner of request
     */
    ONLY_FOLLOWINGS,
    /**
     * Returns notes only from users, who are ignored by the owner of request
     */
    ONLY_IGNORE
}
