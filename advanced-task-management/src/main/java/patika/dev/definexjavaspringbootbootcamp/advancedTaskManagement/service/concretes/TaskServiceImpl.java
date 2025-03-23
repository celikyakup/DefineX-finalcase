package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskService;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskStateChangeHistoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskStateChangeHistoryService historyService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @CacheEvict(value = "tasks", allEntries = true)
    @Override
    public TaskResponse saveTask(TaskRequest taskRequest) throws MethodArgumentNotValidException {
        Optional<Task> isTaskExist=this.taskRepository.findByTitleAndDescriptionAndReasonAndUserStoryAndAcceptCriteria(taskRequest.getTitle(),taskRequest.getDescription(),taskRequest.getReason(),taskRequest.getUserStory(),taskRequest.getAcceptCriteria());
        if (isTaskExist.isPresent()){
            throw new MethodArgumentNotValidException("Task is already created.");
        }
        if (taskRequest.getStatus().equals(TaskStatus.CANCELLED) || taskRequest.getStatus().equals(TaskStatus.BLOCKED) || taskRequest.getStatus().equals(TaskStatus.COMPLETED)){
            throw new MethodArgumentNotValidException("The status of the newly registered task cannot be " + taskRequest.getStatus().toString().toLowerCase());
        }
        Task task= this.taskRepository.save(this.taskMapper.asEntity(taskRequest));
        return this.taskMapper.asOutput(task);
    }

    @Caching(evict = {
           @CacheEvict(value = "tasks", key = "#id"),
            @CacheEvict(value = "tasks", allEntries = true)
    })
    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest taskUpdateRequest) throws NotFoundException,MethodArgumentNotValidException {
        Optional<Task> taskFromDb= this.taskRepository.findById(id);
        if (taskFromDb.isEmpty()){
            throw new NotFoundException("The task not found with id:" + id);
        }
        if (taskFromDb.get().getStatus().equals(TaskStatus.COMPLETED)){
            throw new MethodArgumentNotValidException("This task completed so no action can be taken!");
        }
        if (taskUpdateRequest.getStatus().equals(TaskStatus.BLOCKED) || taskUpdateRequest.getStatus().equals(TaskStatus.CANCELLED)){
            if (taskUpdateRequest.getReason() == null || taskUpdateRequest.getReason().trim().isEmpty()){
                throw new MethodArgumentNotValidException("Reason must be entered!");
            }
        }
        Task task= taskFromDb.get();
        this.historyService.recordStateChange(task,task.getStatus(),taskUpdateRequest.getStatus(),taskUpdateRequest.getReason());
        this.taskMapper.update(task,taskUpdateRequest);
        return this.taskMapper.asOutput(this.taskRepository.save(task));
    }

    @Cacheable(value = "tasks", key = "#id")
    @Override
    public TaskResponse getTaskById(Long id) throws NotFoundException {
        Optional<Task> taskFromDb=this.taskRepository.findById(id);
        if (taskFromDb.isEmpty()){
            throw new NotFoundException("The task not found with id:" + id);
        }
        return this.taskMapper.asOutput(taskFromDb.get());
    }

    @Cacheable(value = "tasks")
    @Override
    public List<TaskResponse> getAllTasks() {
        return this.taskMapper.asOutput(this.taskRepository.findAll());
    }

    @Cacheable(value = "tasks", key = "#userId")
    @Override
    public List<TaskResponse> getTaskByUserId(Long userId) {
        User user=this.userRepository.findById(userId).orElseThrow(()->new NotFoundException("The user not found with id:" + userId));
        return this.taskMapper.asOutput(this.taskRepository.findByUserId(user.getId()));
    }

    @Cacheable(value = "tasks", key = "#projectId")
    @Override
    public List<TaskResponse> getTaskByProjectId(Long projectId) {
        Project project=this.projectRepository.findById(projectId).orElseThrow(()->new NotFoundException("The project not found with id:" + projectId));
        return this.taskMapper.asOutput(this.taskRepository.findByProjectId(project.getId()));
    }

    @Caching(evict = {
            @CacheEvict(value = "tasks", key = "#id"),
            @CacheEvict(value = "tasks", allEntries = true)
    })
    @Override
    public void delete(Long id) throws NotFoundException {
        Optional<Task> taskFromDb=this.taskRepository.findById(id);
        if (taskFromDb.isEmpty()){
            throw new NotFoundException("The task not found with id:" + id);
        } if (taskFromDb.get().isDeleted()){
            throw new MethodArgumentNotValidException("The task has already been deleted!");
        }
        taskFromDb.get().setDeleted(true);
        this.taskRepository.save(taskFromDb.get());
    }

    @CacheEvict(value = "tasks", key = "#id")
    @Override
    public TaskResponse patchStatus(Long id, TaskPatchRequest taskPatchRequest) throws NotFoundException {
        Task taskFromDb=this.taskRepository.findById(id).orElseThrow(()->new NotFoundException("The task not found with it:" + id));
        if (taskFromDb.getStatus().equals(TaskStatus.COMPLETED)){
            throw new MethodArgumentNotValidException("This task completed so no action can be taken!");
        }
        if (taskPatchRequest.getStatus().equals(TaskStatus.BLOCKED) || taskPatchRequest.getStatus().equals(TaskStatus.CANCELLED)){
            if (taskPatchRequest.getReason() == null || taskPatchRequest.getReason().trim().isEmpty()){
                throw new MethodArgumentNotValidException("Reason must be entered!");
            }
        }
        this.historyService.recordStateChange(taskFromDb,taskFromDb.getStatus(),taskPatchRequest.getStatus(),taskPatchRequest.getReason());
        this.taskMapper.update(taskFromDb,taskPatchRequest);
        return this.taskMapper.asOutput(this.taskRepository.save(taskFromDb));
    }
}
