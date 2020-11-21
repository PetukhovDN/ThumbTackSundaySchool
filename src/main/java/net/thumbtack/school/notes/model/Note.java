package net.thumbtack.school.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private int id;
    private String noteHead;
    private User author;
    private Section section;
    private LocalDateTime creationTime;
    private List<NoteRevision> revisions;
}
