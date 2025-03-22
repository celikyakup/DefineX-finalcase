package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.ProjectStateChangeHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.ProjectStateChangeHistoryMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectStateChangeHistoryRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.ProjectStateChangeHistoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class ProjectStateChangeHistoryServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private ProjectStateChangeHistoryServiceImpl service;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectStateChangeHistoryRepository historyRepository;

    @Mock
    private ProjectStateChangeHistoryMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(service)
                .build();
    }

    @Test
    void recordStateChange_ShouldSaveHistory() {
        Project project = new Project();

        service.recordStateChange(project, ProjectStatus.IN_PROGRESS, ProjectStatus.COMPLETED, "completed");

        verify(historyRepository).save(any(ProjectStateChangeHistory.class));
    }

    @Test
    void getHistoryByProjectId_ShouldReturnList() {
        Project project = new Project();
        project.setId(1L);
        ProjectStateChangeHistory entity = new ProjectStateChangeHistory();
        ProjectStateChangeHistoryResponse response = new ProjectStateChangeHistoryResponse();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(historyRepository.findByProjectId(1L)).thenReturn(List.of(entity));
        when(mapper.asOutput(List.of(entity))).thenReturn(List.of(response));

        List<ProjectStateChangeHistoryResponse> result = service.getHistoryByProjectId(1L);
        assertEquals(1, result.size());
        assertEquals(List.of(response),result);
    }

    @Test
    void getHistoryByProjectId_ShouldThrow404_WhenProjectNotFound() {
        when(projectRepository.findById(150L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getHistoryByProjectId(150L));
    }
}
