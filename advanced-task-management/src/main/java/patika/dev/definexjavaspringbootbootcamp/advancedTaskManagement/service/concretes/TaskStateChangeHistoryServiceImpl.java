package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.TaskStateChangeHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskStateChangeHistoryMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskStateChangeHistoryRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskStateChangeHistoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskStateChangeHistoryServiceImpl implements TaskStateChangeHistoryService {
    private final TaskStateChangeHistoryRepository historyRepository;
    private final TaskRepository taskRepository;
    private final TaskStateChangeHistoryMapper historyMapper;

    @Override
    public void recordStateChange(Task task, TaskStatus oldState, TaskStatus newState, String reason) {
        TaskStateChangeHistory history=new TaskStateChangeHistory();
        history.setTask(task);
        history.setOldState(oldState);
        history.setNewState(newState);
        history.setReason(reason);
        this.historyRepository.save(history);
    }

    @Cacheable(value = "taskChangeHistories", key ="#taskId" )
    @Override
    public List<TaskStateChangeHistoryResponse> getHistoryByTaskId(Long taskId) throws NotFoundException {
        Optional<Task> task= this.taskRepository.findById(taskId);
        if (task.isEmpty()){
            throw new NotFoundException("Task is not found with id" + taskId);
        }
        return this.historyMapper.asOutput(historyRepository.findByTaskId(taskId));
    }
}
