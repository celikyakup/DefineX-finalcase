package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

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
public class ProjectUpdateRequest implements Serializable {
    private String title;
    private String description;
    private ProjectStatus status;
    private String department;
    private String reason;
}
