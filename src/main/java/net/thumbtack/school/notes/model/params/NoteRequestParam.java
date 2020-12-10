// REVU это параметры поиска, к модели не относится
package net.thumbtack.school.notes.model.params;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.thumbtack.school.notes.enums.ParamInclude;
import net.thumbtack.school.notes.enums.ParamSort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestParam {
    int sectionId;
    ParamSort sortByRating;
    List<String> tags;
    boolean allTags;
    LocalDateTime timeFrom;
    LocalDateTime timeTo;
    int noteUserId;
    ParamInclude include;
    boolean comments;
    boolean allVersions;
    boolean commentVersion;
    int from;
    int count;
}
