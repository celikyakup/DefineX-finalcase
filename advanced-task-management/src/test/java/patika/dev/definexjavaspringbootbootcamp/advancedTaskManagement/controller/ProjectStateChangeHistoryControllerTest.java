package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config.GlobalExceptionHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectStateChangeHistoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectStateChangeHistoryControllerTest {

    private static final String API_BASE_PATH = "/v1/project-state-change-history";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProjectStateChangeHistoryController projectStateChangeHistoryController;

    @Mock
    private ProjectStateChangeHistoryService projectStateChangeHistoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectStateChangeHistoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getHistoryByProjectId_ShouldReturnHistoryList() throws Exception {
        ProjectResponse projectResponse=new ProjectResponse();
        projectResponse.setId(1L);

        ProjectStateChangeHistoryResponse h1 = new ProjectStateChangeHistoryResponse();
        h1.setId(1L);
        h1.setProject(projectResponse);
        h1.setOldState(ProjectStatus.IN_PROGRESS);
        h1.setNewState(ProjectStatus.COMPLETED);
        h1.setReason("Project completed");

        ProjectStateChangeHistoryResponse h2 = new ProjectStateChangeHistoryResponse();
        h2.setId(2L);
        h2.setProject(projectResponse);
        h2.setOldState(ProjectStatus.IN_PROGRESS);
        h2.setNewState(ProjectStatus.CANCELLED);
        h2.setReason("Cancelled work");

        when(projectStateChangeHistoryService.getHistoryByProjectId(eq(projectResponse.getId())))
                .thenReturn(List.of(h1, h2));

        String apiPath= API_BASE_PATH + "/project/" + projectResponse.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].oldState").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[1].newState").value("CANCELLED"));
    }

    @Test
    void getHistoryByProjectId_ShouldReturn404_WhenProjectNotFound() throws Exception {
        Long randomId= 999L;
        when(projectStateChangeHistoryService.getHistoryByProjectId(eq(randomId)))
                .thenThrow(new NotFoundException("Project is not found with id:" + randomId));

        String apiPath= API_BASE_PATH + "/project/" + randomId;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project is not found with id:" + randomId));
    }
}
