package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskPatchRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.TaskUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.TaskService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(){
        return ResponseEntity.ok(this.taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(this.taskService.getTaskById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTaskByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(this.taskService.getTaskByUserId(userId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTaskByProjectId(@PathVariable Long projectId){
        return ResponseEntity.ok(this.taskService.getTaskByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.taskService.saveTask(taskRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest taskUpdateRequest){
        return ResponseEntity.ok(this.taskService.updateTask(id,taskUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        this.taskService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patchStatus(@PathVariable Long id,@Valid @RequestBody TaskPatchRequest taskPatchRequest){
        return ResponseEntity.ok(this.taskService.patchStatus(id, taskPatchRequest));
    }
}
