package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface TaskService {
    TaskResponse saveTask(TaskRequest taskRequest) throws MethodArgumentNotValidException;
    TaskResponse updateTask(Long id, TaskUpdateRequest taskUpdateRequest) throws NotFoundException,MethodArgumentNotValidException;
    TaskResponse getTaskById(Long id) throws NotFoundException;
    List<TaskResponse> getAllTasks();
    List<TaskResponse> getTaskByUserId(Long userId);
    List<TaskResponse> getTaskByProjectId(Long projectId);
    void delete(Long id) throws NotFoundException;
    TaskResponse patchStatus(Long id, TaskPatchRequest taskPatchRequest) throws NotFoundException;
}
