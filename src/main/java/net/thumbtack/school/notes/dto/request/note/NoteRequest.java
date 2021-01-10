package net.thumbtack.school.notes.dto.request.note;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteRequest {
    @NotNull
    @NotEmpty
    String subject;

    @NotNull
    @NotEmpty
    String body;

    @NotNull
    int sectionId;
}
