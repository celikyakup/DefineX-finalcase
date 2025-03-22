package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoRequest implements Serializable {
    private String name;
    @Email(message = "Email should be valid!")
    @NotBlank(message = "Email is mandatory!")
    private String email;
    private String password;
    private UserRole role;
}
