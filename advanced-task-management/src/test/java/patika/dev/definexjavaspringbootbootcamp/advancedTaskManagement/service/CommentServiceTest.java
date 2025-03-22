package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Comment;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.CommentMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.CommentRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentService)
                .build();
    }

    @Test
    void addCommentToTask_ShouldSaveComment() {
        CommentRequest request = new CommentRequest();
        request.setContent("test");

        Task task = new Task();
        task.setId(1L);
        TaskResponse taskResponse=new TaskResponse();
        Comment entity = new Comment();
        CommentResponse response = new CommentResponse();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.asOutput(task)).thenReturn(taskResponse);
        when(commentMapper.asEntity(request)).thenReturn(entity);
        when(commentRepository.save(entity)).thenReturn(entity);
        when(commentMapper.asOutput(entity)).thenReturn(response);

        CommentResponse result = commentService.addCommentToTask(task.getId(), request);
        assertEquals(response, result);
    }

    @Test
    void addCommentToTask_ShouldThrowNotFound_WhenTaskNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> commentService.addCommentToTask(1L, new CommentRequest()));
    }

    @Test
    void getCommentsByTaskId_ShouldReturnList() {
        Task task = new Task();
        task.setId(1L);
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(task.getId())).thenReturn(List.of(comment));
        when(commentMapper.asOutput(comment)).thenReturn(response);

        List<CommentResponse> result = commentService.getCommentsByTaskId(task.getId());
        assertEquals(1, result.size());
        assertEquals(List.of(response),result);
    }

    @Test
    void getCommentsByTaskId_ShouldReturnNotFoundException() {
        Long taskId = 100L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> commentService.getCommentsByTaskId(taskId));

        assertEquals("Task not found with id:" + taskId, exception.getMessage());
        verify(taskRepository).findById(taskId);
        verifyNoInteractions(commentRepository);
    }

    @Test
    void deleteComment_ShouldSetDeletedTrue() {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.deleteComment(comment.getId());

        assertTrue(comment.isDeleted());
        verify(commentRepository).save(comment);
    }
}
