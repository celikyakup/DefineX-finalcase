package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config.GlobalExceptionHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest {
    private static final String API_BASE_PATH= "/v1/comments";
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private ObjectMapper objectMapper= new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createProject_ShouldReturnOk() throws Exception {
        TaskResponse task=new TaskResponse();
        task.setId(1L);

        CommentRequest commentRequest=new CommentRequest();
        commentRequest.setContent("test");

        CommentResponse commentResponse=new CommentResponse();
        commentResponse.setId(1L);
        commentResponse.setContent("test");

        when(commentService.addCommentToTask(eq(task.getId()),any(CommentRequest.class))).thenReturn(commentResponse);

        String apiPath = API_BASE_PATH + "/task/" + task.getId() ;
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("test"));

    }

    @Test
    void addComment_ShouldReturnValidationError_WhenContentEmpty() throws Exception {
        TaskResponse task=new TaskResponse();
        task.setId(1L);

        CommentRequest request = new CommentRequest();
        request.setContent("");
        request.setTask(task);

        String apiPath=API_BASE_PATH + "/task/" + task.getId();
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addComment_ShouldReturn404_WhenTaskNotFound() throws Exception {
        TaskResponse task=new TaskResponse();
        task.setId(1L);

        CommentRequest request = new CommentRequest();
        request.setContent("Test comment");
        request.setTask(task);

        when(commentService.addCommentToTask(eq(999L), any()))
                .thenThrow(new NotFoundException("Task not found with id: 999"));

        String apiPath=API_BASE_PATH + "/task/999" ;
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"));
    }

    @Test
    void getCommentsByTaskId_ShouldReturnOk() throws Exception {
        TaskResponse task= new TaskResponse();
        task.setId(1L);

        CommentResponse commentResponse1 = new CommentResponse();
        commentResponse1.setId(1L);
        commentResponse1.setContent("Comment 1");
        commentResponse1.setTask(task);

        CommentResponse commentResponse2 = new CommentResponse();
        commentResponse2.setId(2L);
        commentResponse2.setContent("Comment 2");
        commentResponse2.setTask(task);

        when(commentService.getCommentsByTaskId(1L)).thenReturn(List.of(commentResponse1, commentResponse2));

        String apiPath=API_BASE_PATH + "/task/" + task.getId() ;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getCommentsByTaskId_ShouldReturn404_WhenTaskNotFound() throws Exception {
        when(commentService.getCommentsByTaskId(100L))
                .thenThrow(new NotFoundException("Task not found with id: 100"));

        String apiPath= API_BASE_PATH + "/task/" + 100L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id: 100"));
    }

    @Test
    void deleteComment_ShouldReturnOk() throws Exception {
        TaskResponse task= new TaskResponse();
        task.setId(1L);

        CommentResponse commentResponse=new CommentResponse();
        commentResponse.setContent("content");
        commentResponse.setTask(task);
        commentResponse.setId(1L);

        doNothing().when(commentService).deleteComment(commentResponse.getId());

        String apiPath= API_BASE_PATH + "/" + commentResponse.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment_ShouldReturn404_WhenCommentNotFound() throws Exception {
        Long randomId= 155L;

        doThrow(new NotFoundException("Comment not found with id:" + randomId))
                .when(commentService).deleteComment(randomId);

        String apiPath= API_BASE_PATH + "/" + randomId;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found with id:" + randomId));
    }
}
