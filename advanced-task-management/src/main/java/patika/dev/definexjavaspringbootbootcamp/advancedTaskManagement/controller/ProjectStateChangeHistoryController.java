package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectStateChangeHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project-state-change-history")
public class ProjectStateChangeHistoryController {

    private final ProjectStateChangeHistoryService projectStateChangeHistoryService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectStateChangeHistoryResponse>> getProjectStateHistoryByTaskId(@PathVariable("projectId") Long id){
        List<ProjectStateChangeHistoryResponse> historyList= projectStateChangeHistoryService.getHistoryByProjectId(id);
        return ResponseEntity.ok(historyList);
    }
}
