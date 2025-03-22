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
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest {

    private static final String API_BASE_PATH = "/v1/projects";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findProject_ShouldReturnOk() throws Exception {
        ProjectResponse projectResponse1=new ProjectResponse();
        projectResponse1.setStatus(ProjectStatus.IN_PROGRESS);
        projectResponse1.setReason("test");
        projectResponse1.setId(1L);
        projectResponse1.setDepartment("project");

        ProjectResponse projectResponse2=new ProjectResponse();
        projectResponse2.setStatus(ProjectStatus.IN_PROGRESS);
        projectResponse2.setReason("test2");
        projectResponse2.setId(2L);
        projectResponse2.setDepartment("test2");

        when(projectService.getAllProjects()).thenReturn(List.of(projectResponse1,projectResponse2));

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value(ProjectStatus.IN_PROGRESS.toString()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].status").value(ProjectStatus.IN_PROGRESS.toString()));
    }

    @Test
    void createProject_ShouldReturnOk() throws Exception {
        ProjectRequest request= new ProjectRequest();
        request.setDepartment("project");
        request.setStatus(ProjectStatus.IN_PROGRESS);
        request.setReason("test");

        ProjectResponse projectResponse1=new ProjectResponse();
        projectResponse1.setStatus(ProjectStatus.IN_PROGRESS);
        projectResponse1.setReason("test");
        projectResponse1.setId(1L);
        projectResponse1.setDepartment("project");

        when(projectService.saveProject(any(ProjectRequest.class))).thenReturn(projectResponse1);

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reason").value("test"))
                .andExpect(jsonPath("$.department").value("project"));

    }

    @Test
    void findProjectById_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse1=new ProjectResponse();
        projectResponse1.setStatus(ProjectStatus.IN_PROGRESS);
        projectResponse1.setReason("test");
        projectResponse1.setId(1L);
        projectResponse1.setDepartment("project");

        when(projectService.getProjectById(1L)).thenReturn(projectResponse1);

        String apiPath= API_BASE_PATH + "/" + projectResponse1.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reason").value("test"))
                .andExpect(jsonPath("$.department").value("project"));
    }

    @Test
    void updateProject_ShouldReturnOk() throws Exception{
        ProjectUpdateRequest request=new ProjectUpdateRequest();
        request.setStatus(ProjectStatus.IN_PROGRESS);
        request.setReason("test");
        request.setDepartment("project");

        ProjectResponse projectResponse1=new ProjectResponse();
        projectResponse1.setStatus(ProjectStatus.IN_PROGRESS);
        projectResponse1.setReason("test");
        projectResponse1.setId(1L);
        projectResponse1.setDepartment("test");

        when(projectService.updateProject(eq(1L),any(ProjectUpdateRequest.class))).thenReturn(projectResponse1);

        String apiPath= API_BASE_PATH + "/" + projectResponse1.getId();
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reason").value("test"))
                .andExpect(jsonPath("$.department").value("test"));
    }

    @Test
    void deleteProject_ShouldReturnOk() throws Exception{
        ProjectResponse projectResponse=new ProjectResponse();
        projectResponse.setId(1L);

        doNothing().when(projectService).delete(projectResponse.getId());

        String apiPath= API_BASE_PATH + "/" + projectResponse.getId();
        mockMvc.perform(delete(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectResponse)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProject_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        ProjectUpdateRequest request=new ProjectUpdateRequest();
        request.setStatus(ProjectStatus.COMPLETED);

        when(projectService.updateProject(eq(1L),any(ProjectUpdateRequest.class))).thenThrow(new MethodArgumentNotValidException("error"));
        String apiPath= API_BASE_PATH + "/1";
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("error"));
    }

    @Test
    void updateProject_ShouldReturn404WhenTaskNotFound() throws Exception {
        ProjectUpdateRequest request=new ProjectUpdateRequest();
        request.setStatus(ProjectStatus.COMPLETED);
        request.setTitle("test");

        when(projectService.updateProject(eq(200L), any()))
                .thenThrow(new NotFoundException("Project not found!"));

        String apiPath = API_BASE_PATH + "/200";
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found!"));
    }

    @Test
    void findProjectById_ShouldReturn404WhenTaskNotFound() throws Exception{

        when(projectService.getProjectById(990L)).thenThrow(new NotFoundException("Project not found!"));

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found!"));
    }

    @Test
    void deleteTaskById_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        doThrow(new NotFoundException("Task not found!")).when(projectService).delete(990L);

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found!"));
    }
}
