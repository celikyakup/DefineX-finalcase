package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.CommentService;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/task/{taskId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long taskId, @RequestBody @Valid CommentRequest commentRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.commentService.addCommentToTask(taskId, commentRequest));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTask(@PathVariable Long taskId){
        return ResponseEntity.ok(this.commentService.getCommentsByTaskId(taskId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommit(@PathVariable Long commentId){
        this.commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
