package net.thumbtack.school.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private int id;
    private String noteHead;
    private Map<Integer, String> noteBodyWithRevision;
    private User author;
    private Section section;
    private LocalDateTime creationTime;
}
