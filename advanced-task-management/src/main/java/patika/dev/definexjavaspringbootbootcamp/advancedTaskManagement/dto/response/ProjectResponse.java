package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private ProjectStatus status;
    private String reason;
    private String department;
    private List<UserInfoResponse> teamMembers;
}
