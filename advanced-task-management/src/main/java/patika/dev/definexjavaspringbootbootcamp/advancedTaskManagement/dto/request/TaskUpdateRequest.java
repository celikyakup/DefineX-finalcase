package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskPriority;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest implements Serializable {
    private String title;

    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    private String reason;

    private String userStory;

    private String acceptCriteria;
}
