package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse implements Serializable {

    private Long id;
    private String name;

    @Email(message = "Email should be valid!")
    private String email;
    private UserRole role;
}
