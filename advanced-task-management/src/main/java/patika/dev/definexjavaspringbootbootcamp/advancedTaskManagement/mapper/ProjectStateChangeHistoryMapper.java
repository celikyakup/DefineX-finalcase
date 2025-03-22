package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.ProjectStateChangeHistory;

import java.util.List;

@Mapper
public interface ProjectStateChangeHistoryMapper {
    @Mapping(source = "changeAt",target = "changeAt",dateFormat = "yyyy-MM-dd HH:mm")
    List<ProjectStateChangeHistoryResponse> asOutput(List<ProjectStateChangeHistory> histories);
}
