package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface ProjectService {
    ProjectResponse saveProject(ProjectRequest projectRequest) throws MethodArgumentNotValidException;
    ProjectResponse updateProject(Long id,ProjectUpdateRequest projectUpdateRequest) throws NotFoundException,MethodArgumentNotValidException;
    ProjectResponse getProjectById(Long id) throws NotFoundException;
    List<ProjectResponse> getAllProjects();
    void delete (Long id) throws NotFoundException;
}
