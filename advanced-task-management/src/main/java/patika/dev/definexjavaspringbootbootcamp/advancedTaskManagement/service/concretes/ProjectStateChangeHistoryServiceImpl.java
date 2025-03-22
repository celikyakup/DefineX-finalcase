package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.ProjectStateChangeHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.ProjectStateChangeHistoryMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectStateChangeHistoryRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectStateChangeHistoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectStateChangeHistoryServiceImpl implements ProjectStateChangeHistoryService {
    private final ProjectStateChangeHistoryRepository projectHistoryRepo;
    private final ProjectRepository projectRepository;
    private final ProjectStateChangeHistoryMapper projectStateChangeHistoryMapper;

    @CacheEvict(value = "projectStateHistories", allEntries = true)
    @Override
    public void recordStateChange(Project project, ProjectStatus oldState, ProjectStatus newState, String reason) {
        ProjectStateChangeHistory projectStateChangeHistory=new ProjectStateChangeHistory();
        projectStateChangeHistory.setProject(project);
        projectStateChangeHistory.setOldState(oldState);
        projectStateChangeHistory.setNewState(newState);
        projectStateChangeHistory.setReason(reason);
        this.projectHistoryRepo.save(projectStateChangeHistory);
    }

    @Cacheable(value = "projectStateHistories", key = "#projectId")
    @Override
    public List<ProjectStateChangeHistoryResponse> getHistoryByProjectId(Long projectId) throws NotFoundException {
        Optional<Project> projectExistDb=this.projectRepository.findById(projectId);
        if (projectExistDb.isEmpty()){
            throw new NotFoundException("Project is not found with id:" + projectId);
        }
        return this.projectStateChangeHistoryMapper.asOutput(this.projectHistoryRepo.findByProjectId(projectId));
    }
}
