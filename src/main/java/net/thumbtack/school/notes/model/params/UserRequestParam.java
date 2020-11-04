package net.thumbtack.school.notes.model.params;

import net.thumbtack.school.notes.enums.ParamSort;
import net.thumbtack.school.notes.enums.ParamType;

public class UserRequestParam {

    private ParamSort sortByRating;
    private ParamType type;
    private int from;
    private int count;

    public UserRequestParam(ParamSort sortByRating, ParamType type, int from, int count) {
        this.sortByRating = sortByRating;
        this.type = type;
        this.from = from;
        this.count = count;
    }

    public ParamSort getSortByRating() {
        return sortByRating;
    }

    public void setSortByRating(ParamSort sortByRating) {
        this.sortByRating = sortByRating;
    }

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
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
