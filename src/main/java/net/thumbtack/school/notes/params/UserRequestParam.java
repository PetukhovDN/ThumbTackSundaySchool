package net.thumbtack.school.notes.params;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.ParamSort;
import net.thumbtack.school.notes.enums.ParamType;

/**
 * User request parameters for getting list of user accounts from the server
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestParam {
    ParamSort sortByRating;
    ParamType type;
    String from;
    String count;
}
