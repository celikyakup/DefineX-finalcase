package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.TaskStateChangeHistory;

import java.util.List;

@Mapper
public interface TaskStateChangeHistoryMapper {
    @Mapping(source = "changeAt",target = "changeAt",dateFormat = "yyyy-MM-dd HH:mm")
    List<TaskStateChangeHistoryResponse> asOutput(List<TaskStateChangeHistory> histories);
}
