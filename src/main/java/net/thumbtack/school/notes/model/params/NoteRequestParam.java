// REVU это параметры поиска, к модели не относится
package net.thumbtack.school.notes.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.enums.ParamInclude;
import net.thumbtack.school.notes.enums.ParamSort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
