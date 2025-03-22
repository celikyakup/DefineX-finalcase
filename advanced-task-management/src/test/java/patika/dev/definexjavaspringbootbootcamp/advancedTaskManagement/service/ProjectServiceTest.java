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
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.ProjectMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectStateChangeHistoryService;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.ProjectServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


public class ProjectServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectStateChangeHistoryService projectStateChangeHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectService)
                .build();
    }

    @Test
    void saveProject_ShouldReturnResponse_WhenProjectNotExists() {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Test"); request.setDescription("Desc"); request.setDepartment("Dev");

        Project project = new Project();
        ProjectResponse response = new ProjectResponse();

        when(projectRepository.findByTitleAndDescriptionAndDepartment(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(projectMapper.asEntity(request)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.asOutput(project)).thenReturn(response);

        ProjectResponse result = projectService.saveProject(request);
        assertEquals(response, result);
    }

    @Test
    void saveProject_ShouldThrow_WhenProjectExists() {
        ProjectRequest request = new ProjectRequest();
        when(projectRepository.findByTitleAndDescriptionAndDepartment(any(), any(), any()))
                .thenReturn(Optional.of(new Project()));

        assertThrows(MethodArgumentNotValidException.class, () -> projectService.saveProject(request));
    }

    @Test
    void updateProject_ShouldThrow_WhenNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.updateProject(1L, new ProjectUpdateRequest()));
    }

    @Test
    void updateProject_ShouldThrow_WhenCompleted() {
        Project p = new Project();
        p.setStatus(ProjectStatus.COMPLETED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));

        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setStatus(ProjectStatus.COMPLETED);

        assertThrows(MethodArgumentNotValidException.class, () -> projectService.updateProject(1L, req));
    }

    @Test
    void updateProject_ShouldThrow_WhenReasonMissingForCancelled() {
        Project project = new Project();
        project.setStatus(ProjectStatus.IN_PROGRESS);

        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setStatus(ProjectStatus.CANCELLED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(MethodArgumentNotValidException.class, () -> projectService.updateProject(1L, req));
    }

    @Test
    void updateProject_ShouldThrow_WhenDeletedIsTrue() {
        Project project = new Project();
        project.setDeleted(true);
        project.setStatus(ProjectStatus.IN_PROGRESS);

        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setStatus(ProjectStatus.COMPLETED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(MethodArgumentNotValidException.class, () -> projectService.updateProject(1L, req));
    }


    @Test
    void updateProject_ShouldReturnUpdated_WhenValid() {
        Project project = new Project();
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setId(1L);

        ProjectUpdateRequest request = new ProjectUpdateRequest();
        request.setStatus(ProjectStatus.COMPLETED);
        request.setReason("Done");

        Project updated = new Project();
        ProjectResponse response = new ProjectResponse();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updated);
        when(projectMapper.asOutput(updated)).thenReturn(response);

        ProjectResponse result = projectService.updateProject(project.getId(), request);
        assertEquals(response, result);
    }

    @Test
    void getProjectById_ShouldReturnProject() {
        Project project = new Project();

        ProjectResponse response = new ProjectResponse();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.asOutput(project)).thenReturn(response);
        assertEquals(response, projectService.getProjectById(1L));
    }

    @Test
    void getProjectById_ShouldReturnNotFoundException() {
        Long projectId=100L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.getProjectById(projectId));
        assertEquals("The project not found with id:" + projectId, exception.getMessage());
        verify(projectRepository).findById(projectId);
    }

    @Test
    void getAllProjects_ShouldReturnList() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project()));
        when(projectMapper.asOutput(anyList())).thenReturn(List.of(new ProjectResponse()));
        assertEquals(1, projectService.getAllProjects().size());
    }

    @Test
    void delete_ShouldSetDeletedFalse() {
        Project project = new Project();
        project.setDeleted(false);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        projectService.delete(1L);

        verify(projectRepository).save(project);
        assertTrue(project.isDeleted());
    }

    @Test
    void delete_ShouldWhenIdNotExist() {
        Project project = new Project();
        project.setId(1L);
        project.setDeleted(false);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.delete(project.getId()));
    }

}
