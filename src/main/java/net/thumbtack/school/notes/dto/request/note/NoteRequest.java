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
    @NotEmpty
    String subject;

    @NotEmpty
    String body;

    @NotNull
    //@Digits(integer = 0, fraction = 0)
    int sectionId;
}
