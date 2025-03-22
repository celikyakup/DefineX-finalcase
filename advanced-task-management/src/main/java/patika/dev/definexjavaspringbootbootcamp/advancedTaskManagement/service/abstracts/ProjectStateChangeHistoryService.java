package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;


import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface ProjectStateChangeHistoryService {
    void recordStateChange(Project project, ProjectStatus oldState, ProjectStatus newState, String reason);
    List<ProjectStateChangeHistoryResponse> getHistoryByProjectId(Long projectId) throws NotFoundException;
}
