package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.ProjectMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.ProjectRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectService;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectStateChangeHistoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectStateChangeHistoryService projectHistoryService;

    @CacheEvict(value = "projects", allEntries = true)
    @Override
    public ProjectResponse saveProject(ProjectRequest projectRequest) throws MethodArgumentNotValidException {
        Optional<Project> isProjectExist = this.projectRepository.findByTitleAndDescriptionAndDepartment(projectRequest.getTitle(), projectRequest.getDescription(), projectRequest.getDepartment());
        if (isProjectExist.isPresent()){
            throw new MethodArgumentNotValidException("The project is already created.");
        }
        Project project=projectRepository.save(this.projectMapper.asEntity(projectRequest));
        return this.projectMapper.asOutput(project);
    }

    @CacheEvict(value = "projects", key = "#id")
    @Override
    public ProjectResponse updateProject(Long id, ProjectUpdateRequest projectUpdateRequest) throws NotFoundException,MethodArgumentNotValidException {
        Optional<Project> projectFromDb= this.projectRepository.findById(id);
        if (projectFromDb.isEmpty()){
            throw new NotFoundException("The project not found with id:" + id);
        }if (projectFromDb.get().getStatus().equals(ProjectStatus.COMPLETED)){
            throw new MethodArgumentNotValidException("This project completed so no action can be taken!");
        }if (projectUpdateRequest.getStatus().equals(ProjectStatus.CANCELLED)){
            if (projectUpdateRequest.getReason() == null || projectUpdateRequest.getReason().trim().isEmpty()){
                throw new MethodArgumentNotValidException("Reason must be entered!");
            }
        }if (projectFromDb.get().isDeleted()){
            throw new MethodArgumentNotValidException("This project has already been deleted!");
        }
        Project project=projectFromDb.get();
        this.projectHistoryService.recordStateChange(project,project.getStatus(),projectUpdateRequest.getStatus(),projectUpdateRequest.getReason());
        this.projectMapper.update(project,projectUpdateRequest);
        return this.projectMapper.asOutput(this.projectRepository.save(project));
    }

    @Cacheable(value = "projects", key = "#id")
    @Override
    public ProjectResponse getProjectById(Long id) throws NotFoundException {
        return this.projectMapper.asOutput(this.projectRepository.findById(id).orElseThrow(()->new NotFoundException("The project not found with id:" + id)));
    }

    @Cacheable(value = "projects")
    @Override
    public List<ProjectResponse> getAllProjects() {
        return this.projectMapper.asOutput(this.projectRepository.findAll());
    }

    @CacheEvict(value = "projects", key = "#id")
    @Override
    public void delete(Long id) throws NotFoundException {
        Optional<Project> projectFromDb=this.projectRepository.findById(id);
        if (projectFromDb.isEmpty()){
            throw new NotFoundException("The project not found with id:" + id);
        }
        if (projectFromDb.get().isDeleted()){
            throw new MethodArgumentNotValidException("The project has already been deleted!");
        }
        projectFromDb.get().setDeleted(true);
        this.projectRepository.save(projectFromDb.get());
    }
}
