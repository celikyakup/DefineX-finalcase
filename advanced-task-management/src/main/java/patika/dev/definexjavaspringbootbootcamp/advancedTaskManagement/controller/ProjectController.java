package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.ProjectUpdateRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.ProjectResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.ProjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(){
        return ResponseEntity.ok(this.projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(this.projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.projectService.saveProject(projectRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,@RequestBody ProjectUpdateRequest projectUpdateRequest){
        return ResponseEntity.ok(this.projectService.updateProject(id,projectUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        this.projectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
