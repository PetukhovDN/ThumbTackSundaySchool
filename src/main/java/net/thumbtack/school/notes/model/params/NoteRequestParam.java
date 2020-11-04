package net.thumbtack.school.notes.model.params;

import net.thumbtack.school.notes.enums.ParamInclude;
import net.thumbtack.school.notes.enums.ParamSort;

import java.time.LocalDateTime;
import java.util.List;

public class NoteRequestParam {
    private int sectionId;
    private ParamSort sortByRating;
    private List<String> tags;
    private boolean allTags;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private int noteUserId;
    private ParamInclude include;
    private boolean comments;
    private boolean allVersions;
    private boolean commentVersion;
    private int from;
    private int count;

    public NoteRequestParam(int sectionId,
                            ParamSort sortByRating,
                            List<String> tags,
                            boolean allTags,
                            LocalDateTime timeFrom,
                            LocalDateTime timeTo,
                            int noteUserId,
                            ParamInclude include,
                            boolean comments,
                            boolean allVersions,
                            boolean commentVersion,
                            int from,
                            int count) {
        this.sectionId = sectionId;
        this.sortByRating = sortByRating;
        this.tags = tags;
        this.allTags = allTags;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.noteUserId = noteUserId;
        this.include = include;
        this.comments = comments;
        this.allVersions = allVersions;
        this.commentVersion = commentVersion;
        this.from = from;
        this.count = count;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public ParamSort getSortByRating() {
        return sortByRating;
    }

    public void setSortByRating(ParamSort sortByRating) {
        this.sortByRating = sortByRating;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isAllTags() {
        return allTags;
    }

    public void setAllTags(boolean allTags) {
        this.allTags = allTags;
    }

    public LocalDateTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalDateTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalDateTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalDateTime timeTo) {
        this.timeTo = timeTo;
    }

    public int getNoteUserId() {
        return noteUserId;
    }

    public void setNoteUserId(int noteUserId) {
        this.noteUserId = noteUserId;
    }

    public ParamInclude getInclude() {
        return include;
    }

    public void setInclude(ParamInclude include) {
        this.include = include;
    }

    public boolean isComments() {
        return comments;
    }

    public void setComments(boolean comments) {
        this.comments = comments;
    }

    public boolean isAllVersions() {
        return allVersions;
    }

    public void setAllVersions(boolean allVersions) {
        this.allVersions = allVersions;
    }

    public boolean isCommentVersion() {
        return commentVersion;
    }

    public void setCommentVersion(boolean commentVersion) {
        this.commentVersion = commentVersion;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
