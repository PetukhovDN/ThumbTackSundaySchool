// REVU это параметры поиска, к модели не относится
package net.thumbtack.school.notes.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.enums.ParamSort;
import net.thumbtack.school.notes.enums.ParamType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestParam {
    private ParamSort sortByRating;
    private ParamType type;
    private int from;
    private int count;
}
