package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.TaskStateChangeHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskStateChangeHistoryMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskStateChangeHistoryRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.TaskStateChangeHistoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskStateChangeHistoryServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private TaskStateChangeHistoryServiceImpl historyService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskStateChangeHistoryRepository historyRepository;

    @Mock
    private TaskStateChangeHistoryMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(historyService)
                .build();
    }

    @Test
    void recordStateChange_ShouldSaveHistory() {
        Task task = new Task();

        historyService.recordStateChange(task, TaskStatus.BACKLOG, TaskStatus.IN_ANALYSIS, "completed");

        verify(historyRepository).save(any(TaskStateChangeHistory.class));
    }

    @Test
    void getHistoryByProjectId_ShouldReturnList() {
        Task task = new Task();
        task.setId(1L);
        TaskStateChangeHistory entity = new TaskStateChangeHistory();
        TaskStateChangeHistoryResponse response = new TaskStateChangeHistoryResponse();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(historyRepository.findByTaskId(1L)).thenReturn(List.of(entity));
        when(mapper.asOutput(List.of(entity))).thenReturn(List.of(response));

        List<TaskStateChangeHistoryResponse> result = historyService.getHistoryByTaskId(1L);
        assertEquals(1, result.size());
        assertEquals(List.of(response),result);
    }

    @Test
    void getHistoryByProjectId_ShouldThrow404_WhenProjectNotFound() {
        when(taskRepository.findById(150L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> historyService.getHistoryByTaskId(150L));
    }
}


