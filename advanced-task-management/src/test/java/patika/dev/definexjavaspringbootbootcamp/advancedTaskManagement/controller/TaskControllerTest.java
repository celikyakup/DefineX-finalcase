package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config.GlobalExceptionHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskPriority;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest {
    private static final String API_BASE_PATH = "/v1/tasks";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findUsers_ShouldReturnOk() throws Exception {
        ProjectResponse projectResponse=new ProjectResponse();
        UserInfoResponse userInfoResponse=new UserInfoResponse();
        TaskResponse task1 = new TaskResponse(1L,"task1","",TaskStatus.BACKLOG, TaskPriority.HIGH,"","","",projectResponse,userInfoResponse);
        TaskResponse task2= new TaskResponse(2L,"task2","",TaskStatus.BACKLOG, TaskPriority.HIGH,"","","",projectResponse,userInfoResponse);
        when(taskService.getAllTasks()).thenReturn(List.of(task1,task2));

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("task1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("task2"));
    }

   @Test
    void createTask_ShouldReturnOk() throws Exception {
        ProjectResponse projectResponse=new ProjectResponse();
        UserInfoResponse userInfoResponse=new UserInfoResponse();

        TaskRequest request= new TaskRequest();
        request.setTitle("test");
        request.setDescription("test");
        request.setStatus(TaskStatus.IN_ANALYSIS);
        request.setPriority(TaskPriority.LOW);
        request.setUser(userInfoResponse);
        request.setProject(projectResponse);

        TaskResponse response= new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setUser(userInfoResponse);
        response.setProject(projectResponse);

        when(taskService.saveTask(any(TaskRequest.class))).thenReturn(response);

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("test"));

    }

    @Test
    void findTaskById_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse=new ProjectResponse();
        UserInfoResponse userInfoResponse=new UserInfoResponse();

        TaskResponse response=new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setUser(userInfoResponse);
        response.setProject(projectResponse);

        when(taskService.getTaskById(1L)).thenReturn(response);

        String apiPath= API_BASE_PATH + "/" + response.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void findTaskByUserId_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse=new ProjectResponse();

        UserInfoResponse userInfoResponse=new UserInfoResponse();
        userInfoResponse.setId(1L);

        TaskResponse response=new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setUser(userInfoResponse);
        response.setProject(projectResponse);

        when(taskService.getTaskByUserId(eq(userInfoResponse.getId()))).thenReturn(List.of(response));

        String apiPath= API_BASE_PATH + "/user/" + userInfoResponse.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("test"))
                .andExpect(jsonPath("$[0].description").value("test"));
    }

    @Test
    void findTaskByProjectId_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse=new ProjectResponse();
        projectResponse.setId(1L);
        UserInfoResponse userInfoResponse=new UserInfoResponse();

        TaskResponse response=new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setUser(userInfoResponse);
        response.setProject(projectResponse);

        when(taskService.getTaskByProjectId(eq(projectResponse.getId()))).thenReturn(List.of(response));

        String apiPath= API_BASE_PATH + "/project/" + projectResponse.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("test"))
                .andExpect(jsonPath("$[0].description").value("test"));
    }


    @Test
    void updateTask_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse=new ProjectResponse();
        UserInfoResponse userInfoResponse=new UserInfoResponse();

        TaskUpdateRequest task = new TaskUpdateRequest("task1","",TaskStatus.BACKLOG, TaskPriority.HIGH,"","","");

        TaskResponse response=new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setUser(userInfoResponse);
        response.setProject(projectResponse);

        when(taskService.updateTask(eq(1L),any(TaskUpdateRequest.class))).thenReturn(response);

        String apiPath= API_BASE_PATH + "/" + response.getId();
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void deleteTask_ShouldReturnOk() throws Exception{
        TaskResponse task= new TaskResponse();
        task.setId(1L);
        task.setTitle("test");

        doNothing().when(taskService).delete(task.getId());

        String apiPath= API_BASE_PATH + "/" + task.getId();
        mockMvc.perform(delete(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());
    }

    @Test
    void patchTaskStatus_ShouldReturnUpdatedTask() throws Exception {
        TaskPatchRequest patchRequest = new TaskPatchRequest();
        patchRequest.setStatus(TaskStatus.IN_ANALYSIS);

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("test");
        response.setDescription("test");
        response.setStatus(TaskStatus.BACKLOG);

        when(taskService.patchStatus(eq(1L), any(TaskPatchRequest.class))).thenReturn(response);
        mockMvc.perform(patch(API_BASE_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateTask_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        TaskUpdateRequest request=new TaskUpdateRequest();
        request.setStatus(TaskStatus.COMPLETED);

        when(taskService.updateTask(eq(1L),any(TaskUpdateRequest.class))).thenThrow(new MethodArgumentNotValidException("error"));
        String apiPath= API_BASE_PATH + "/1";
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        TaskRequest request= new TaskRequest();
        request.setStatus(TaskStatus.CANCELLED);
        request.setTitle("test");
        request.setDescription("test");
        request.setPriority(TaskPriority.CRITICAL);
        request.setUserStory("test");
        request.setReason("test");
        request.setAcceptCriteria("test");
        request.setUser(new UserInfoResponse());
        request.setProject(new ProjectResponse());

        when(taskService.saveTask(any(TaskRequest.class)))
                .thenThrow(new MethodArgumentNotValidException("error"));
        mockMvc.perform(post(API_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ShouldReturn404WhenTaskNotFound() throws Exception {
        TaskUpdateRequest request= new TaskUpdateRequest();
        request.setTitle("test");
        request.setStatus(TaskStatus.BACKLOG);
        request.setPriority(TaskPriority.CRITICAL);
        when(taskService.updateTask(eq(200L), any()))
                .thenThrow(new NotFoundException("User not found!"));

        String apiPath = API_BASE_PATH + "/200";
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

    @Test
    void findTaskById_ShouldReturn404WhenTaskNotFound() throws Exception{
        when(taskService.getTaskById(990L)).thenThrow(new NotFoundException("User not found!"));

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

    @Test
    void deleteTaskById_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        doThrow(new NotFoundException("Task not found!")).when(taskService).delete(990L);

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found!"));
    }

    @Test
    void patchStatus_ShouldReturnValidationError() throws Exception {
        TaskPatchRequest request = new TaskPatchRequest();
        request.setStatus(null);

        String apiPath= API_BASE_PATH + "/1" ;
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchStatus_ShouldReturn404WhenNotFound() throws Exception {
        TaskPatchRequest request = new TaskPatchRequest();
        request.setStatus(TaskStatus.IN_DEVELOPMENT);

        when(taskService.patchStatus(eq(123L), any(TaskPatchRequest.class)))
                .thenThrow(new NotFoundException("Task not found"));

        String apiPath= API_BASE_PATH + "/123" ;
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }
}
