package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskPatchRequest implements Serializable {
 @NotNull
 TaskStatus status;
 String reason;
}
