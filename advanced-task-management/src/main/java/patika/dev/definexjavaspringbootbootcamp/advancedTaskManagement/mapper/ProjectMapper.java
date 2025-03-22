package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {
    Project asEntity(ProjectRequest projectRequest);

    ProjectResponse asOutput(Project project);

    List<ProjectResponse> asOutput(List<Project> projects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Project entity, ProjectUpdateRequest projectUpdateRequest);
}
