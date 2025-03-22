package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

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
public class TaskResponse implements Serializable {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String reason;

    private String userStory;

    private String acceptCriteria;

    private ProjectResponse project;

    private UserInfoResponse user;
}
