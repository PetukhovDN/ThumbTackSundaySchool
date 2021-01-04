package net.thumbtack.school.notes.enums;

/**
 * Types of the returned list of user accounts after user request
 */
public enum ParamType {
    /**
     * Returns accounts with high rating
     */
    HIGH_RATING,
    /**
     * Returns accounts with low rating
     */
    LOW_RATING,
    /**
     * Returns accounts that owner of request is following
     */
    FOLLOWING,
    /**
     * Returns accounts of users which follow owner of the request
     */
    FOLLOWERS,
    /**
     * Returns accounts that owner of request is ignoring
     */
    IGNORE,
    /**
     * Returns accounts of users which ignore owner of the request
     */
    IGNORED_BY,
    /**
     * Returns accounts of users which have left the server
     */
    DELETED,
    /**
     * Returns accounts of users with super root
     */
    ADMIN
}
