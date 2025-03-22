package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest implements Serializable {
    private String title;
    private String description;
    private ProjectStatus status;
    private String reason;
    private String department;
    private List<UserInfoResponse> teamMembers;
}
