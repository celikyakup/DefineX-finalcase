package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;

import java.util.List;

@Mapper
public interface TaskMapper {
    Task asEntity(TaskRequest taskRequest);

    TaskResponse asOutput(Task task);

    List<TaskResponse> asOutput(List<Task> tasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Task entity, TaskUpdateRequest taskUpdateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Task entity, TaskPatchRequest taskPatchRequest);
}
