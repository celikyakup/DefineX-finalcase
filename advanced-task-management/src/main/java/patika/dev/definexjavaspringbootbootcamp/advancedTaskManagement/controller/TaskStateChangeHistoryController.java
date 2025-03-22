package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskStateChangeHistoryResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskStateChangeHistoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/task-state-history")
@RequiredArgsConstructor
public class TaskStateChangeHistoryController {

    private final TaskStateChangeHistoryService historyService;

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskStateChangeHistoryResponse>> getTaskStateHistoryByTaskId(@PathVariable("taskId") Long id){
        List<TaskStateChangeHistoryResponse> historyList= historyService.getHistoryByTaskId(id);
        return ResponseEntity.ok(historyList);
    }
}
