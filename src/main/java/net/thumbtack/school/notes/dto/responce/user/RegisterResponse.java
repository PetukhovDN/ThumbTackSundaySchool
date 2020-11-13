package net.thumbtack.school.notes.dto.responce.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
}
