package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Comment;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.CommentMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.CommentRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @CacheEvict(value = "comments", allEntries = true)
    @Override
    public CommentResponse addCommentToTask(Long taskId, CommentRequest commentRequest) throws NotFoundException {
        Task task=this.taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task not found with id:" + taskId));
        commentRequest.setTask(this.taskMapper.asOutput(task));
        return this.commentMapper.asOutput(this.commentRepository.save(this.commentMapper.asEntity(commentRequest)));
    }

    @Cacheable(value = "comments", key = "#id")
    @Override
    public List<CommentResponse> getCommentsByTaskId(Long id) throws NotFoundException {
        Optional<Task> taskExistDb= this.taskRepository.findById(id);
        if (taskExistDb.isEmpty()){
            throw new NotFoundException("Task not found with id:" + id);
        }
        return commentRepository.findByTaskId(taskExistDb.get().getId())
                .stream().map(this.commentMapper::asOutput)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "comments", key = "#commentId")
    @Override
    public void deleteComment(Long commentId) throws NotFoundException {
        Comment comment=this.commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("Comment not found with id:" + commentId));
        if (comment.isDeleted()){
            throw new MethodArgumentNotValidException("The comment has already been deleted!");
        }
        comment.setDeleted(true);
        this.commentRepository.save(comment);
    }
}
