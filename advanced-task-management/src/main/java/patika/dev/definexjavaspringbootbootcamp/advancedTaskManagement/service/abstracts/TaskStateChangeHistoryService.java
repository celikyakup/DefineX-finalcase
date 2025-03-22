package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface TaskStateChangeHistoryService {
    void recordStateChange(Task task, TaskStatus oldState, TaskStatus newState, String reason);
    List<TaskStateChangeHistoryResponse> getHistoryByTaskId(Long taskId) throws NotFoundException;
}
