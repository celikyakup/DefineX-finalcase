package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface CommentService {
    CommentResponse addCommentToTask (Long taskId, CommentRequest commentRequest) throws NotFoundException;
    List<CommentResponse> getCommentsByTaskId(Long id) throws NotFoundException;
    void deleteComment(Long commentId) throws NotFoundException;
}
