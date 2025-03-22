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
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskStateChangeHistoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskStateChangeHistoryControllerTest {
    private static final String API_BASE_PATH = "/v1/task-state-history";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TaskStateChangeHistoryController taskStateChangeHistoryController;

    @Mock
    private TaskStateChangeHistoryService taskStateChangeHistoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskStateChangeHistoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getHistoryByProjectId_ShouldReturnHistoryList() throws Exception {
        TaskResponse taskResponse=new TaskResponse();
        taskResponse.setId(1L);

        TaskStateChangeHistoryResponse h1 = new TaskStateChangeHistoryResponse();
        h1.setId(1L);
        h1.setTask(taskResponse);
        h1.setOldState(TaskStatus.IN_ANALYSIS);
        h1.setNewState(TaskStatus.COMPLETED);
        h1.setReason("Project completed");

        TaskStateChangeHistoryResponse h2 = new TaskStateChangeHistoryResponse();
        h2.setId(2L);
        h2.setTask(taskResponse);
        h2.setOldState(TaskStatus.BACKLOG);
        h2.setNewState(TaskStatus.IN_DEVELOPMENT);
        h2.setReason("Cancelled work");

        when(taskStateChangeHistoryService.getHistoryByTaskId(eq(taskResponse.getId())))
                .thenReturn(List.of(h1, h2));

        String apiPath= API_BASE_PATH + "/task/" + taskResponse.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].oldState").value(TaskStatus.IN_ANALYSIS.toString()))
                .andExpect(jsonPath("$[1].newState").value(TaskStatus.IN_DEVELOPMENT.toString()));
    }

    @Test
    void getHistoryByProjectId_ShouldReturn404_WhenProjectNotFound() throws Exception {
        Long randomId= 999L;
        when(taskStateChangeHistoryService.getHistoryByTaskId(eq(randomId)))
                .thenThrow(new NotFoundException("Task is not found with id:" + randomId));

        String apiPath= API_BASE_PATH + "/task/" + randomId;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task is not found with id:" + randomId));
    }
}
