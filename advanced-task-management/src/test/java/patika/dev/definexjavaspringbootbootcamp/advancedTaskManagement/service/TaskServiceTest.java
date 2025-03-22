package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskPriority;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskStateChangeHistoryService;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.TaskServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskStateChangeHistoryService taskStateChangeHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskService)
                .build();
    }

    @Test
    void saveTask_ShouldThrowException_WhenTaskAlreadyExists() {
        TaskRequest request = new TaskRequest();
        request.setTitle("T1"); request.setDescription("D1");
        request.setUserStory("US"); request.setReason("R"); request.setAcceptCriteria("AC");

        when(taskRepository.findByTitleAndDescriptionAndReasonAndUserStoryAndAcceptCriteria(
                any(), any(), any(), any(), any())).thenReturn(Optional.of(new Task()));

        assertThrows(MethodArgumentNotValidException.class, () -> taskService.saveTask(request));
    }

    @Test
    void saveTask_ShouldThrowException_WhenStatusIsCancelledOrBlocked() {
        TaskRequest request = new TaskRequest();
        request.setTitle("T1"); request.setStatus(TaskStatus.CANCELLED);

        when(taskRepository.findByTitleAndDescriptionAndReasonAndUserStoryAndAcceptCriteria(
                any(), any(), any(), any(), any())).thenReturn(Optional.empty());

        assertThrows(MethodArgumentNotValidException.class, () -> taskService.saveTask(request));
    }

    @Test
    void saveTask_ShouldReturnResponse_WhenValid() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Task");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.IN_ANALYSIS);

        Task entity = new Task();
        TaskResponse response = new TaskResponse();

        when(taskRepository.findByTitleAndDescriptionAndReasonAndUserStoryAndAcceptCriteria(any(), any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        when(taskMapper.asEntity(request)).thenReturn(entity);
        when(taskRepository.save(entity)).thenReturn(entity);
        when(taskMapper.asOutput(entity)).thenReturn(response);

        TaskResponse result = taskService.saveTask(request);
        assertEquals(response, result);
    }

    @Test
    void getAllTasks_ShouldReturnList() {
        List<Task> taskList = List.of(new Task(), new Task());
        List<TaskResponse> dtoList = List.of(new TaskResponse(), new TaskResponse());

        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskMapper.asOutput(taskList)).thenReturn(dtoList);

        List<TaskResponse> result = taskService.getAllTasks();
        assertEquals(2, result.size());
    }

    @Test
    void getTaskById_ShouldThrow_WhenNotFound() {
        Long incorrectTaskId=100L;
        when(taskRepository.findById(incorrectTaskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(incorrectTaskId));
        verify(taskRepository).findById(incorrectTaskId);
    }

    @Test
    void getTaskByUserId_ShouldReturnList() {
        User user=new User();
        user.setId(1L);

        Task task1=new Task();
        task1.setId(1L);
        task1.setUser(user);
        Task task2=new Task();
        task2.setId(2L);
        task2.setUser(user);

        List<Task> taskList = List.of(task1,task2);

        TaskResponse taskResponse1=new TaskResponse();
        TaskResponse taskResponse2=new TaskResponse();
        List<TaskResponse> responseList = List.of(taskResponse1,taskResponse2);

        when(taskRepository.findByUserId(user.getId())).thenReturn(taskList);
        when(taskMapper.asOutput(taskList)).thenReturn(responseList);

        assertEquals(2, responseList.size());
    }

    @Test
    void getTaskByUserId_ShouldThrow_WhenNotFound() {
        when(taskRepository.findByUserId(100L)).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> taskService.getTaskByUserId(100L));

        assertEquals("The user not found with id:" + 100L , exception.getMessage());
        verify(userRepository).findById(100L);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTaskByProjectId_ShouldReturnList() {
        List<Task> list = List.of(new Task());
        List<TaskResponse> responseList = List.of(new TaskResponse());

        when(taskRepository.findByProjectId(1L)).thenReturn(list);
        when(taskMapper.asOutput(list)).thenReturn(responseList);

        assertEquals(1, responseList.size());
    }

    @Test
    void getTaskByProjectId_ShouldThrow_WhenNotFound() {
        when(taskRepository.findByProjectId(100L)).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> taskService.getTaskByProjectId(100L));

        assertEquals("The project not found with id:" + 100L, exception.getMessage());
        verify(projectRepository).findById(100L);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void delete_ShouldSetDeletedTrue_WhenTaskExists() {
        Task task = new Task(); task.setDeleted(false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);
        verify(taskRepository).save(task);
        assertTrue(task.isDeleted());
    }

    @Test
    void delete_ShouldThrow_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.delete(99L));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenValid() {
        Task existingTask = new Task();
        existingTask.setPriority(TaskPriority.LOW);
        existingTask.setStatus(TaskStatus.BACKLOG);

        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setPriority(TaskPriority.LOW);
        updateRequest.setStatus(TaskStatus.BACKLOG);

        Task updatedTask = new Task();
        TaskResponse taskResponse = new TaskResponse();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.asOutput(updatedTask)).thenReturn(taskResponse);

        TaskResponse result = taskService.updateTask(1L, updateRequest);

        assertNotNull(result);
        assertEquals(taskResponse, result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).asOutput(updatedTask);
    }

    @Test
    void updateTask_ShouldThrow_WhenTaskNotFound() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(99L, request));
        verify(taskRepository).findById(99L);
    }

    @Test
    void patchStatus_ShouldReturnUpdatedTask_WhenValid() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.IN_ANALYSIS);
        task.setDeleted(false);

        TaskPatchRequest request = new TaskPatchRequest();
        request.setStatus(TaskStatus.IN_DEVELOPMENT);
        request.setReason("Work started");

        Task updatedTask = new Task();
        TaskResponse response = new TaskResponse();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(taskMapper.asOutput(updatedTask)).thenReturn(response);

        TaskResponse result = taskService.patchStatus(1L, request);

        assertNotNull(result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(taskMapper).asOutput(updatedTask);
    }

    @Test
    void patchStatus_ShouldThrow_WhenStatusIsSame() {
        Task task = new Task();
        task.setStatus(TaskStatus.IN_DEVELOPMENT);
        task.setDeleted(false);
        task.setId(1L);

        TaskPatchRequest request = new TaskPatchRequest();

        when(taskRepository.findById(199L)).thenReturn(Optional.of(task));

        assertThrows(NotFoundException.class, () -> taskService.patchStatus(1L, request));
    }


}
