package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStateChangeHistoryResponse implements Serializable {
    private Long id;

    private ProjectStatus oldState;

    private ProjectStatus newState;

    private String reason;

    private String changedAt;

    private ProjectResponse project;

    private UserInfoResponse changedBy;
}
