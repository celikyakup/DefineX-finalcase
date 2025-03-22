package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

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
public class TaskStateChangeHistoryResponse implements Serializable {
    private Long id;

    private TaskStatus oldState;

    private TaskStatus newState;

    private String reason;

    private String changedAt;

    private TaskResponse task;

    private UserInfoResponse changedBy;
}
