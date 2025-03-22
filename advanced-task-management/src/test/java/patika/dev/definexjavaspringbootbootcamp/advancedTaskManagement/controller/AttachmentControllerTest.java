package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config.GlobalExceptionHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.AttachmentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AttachmentControllerTest {

    private static final String API_BASE_PATH= "/v1/attachments";
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    private ObjectMapper objectMapper= new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void uploadFile_ShouldReturnCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testfile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "file content".getBytes()
        );
        TaskResponse taskResponse= new TaskResponse();
        taskResponse.setId(1L);

        AttachmentResponse response = new AttachmentResponse();
        response.setId(1L);
        response.setFileName("testfile.txt");
        response.setTask(taskResponse);

        when(attachmentService.uploadFileToTask(eq(taskResponse.getId()), any(MultipartFile.class))).thenReturn(response);

        String apiPath= API_BASE_PATH + "/upload/" + taskResponse.getId();
        mockMvc.perform(multipart(apiPath)
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fileName").value("testfile.txt"));

    }

    @Test
    void uploadFile_ShouldReturn404_WhenTaskNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testfile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "file content".getBytes()
        );

        TaskResponse task= new TaskResponse();
        task.setId(1L);

        AttachmentResponse response= new AttachmentResponse();
        response.setId(1L);
        response.setFileName("testfile.txt");
        response.setTask(task);

        when(attachmentService.uploadFileToTask(eq(99L), any(MultipartFile.class)))
                .thenThrow(new NotFoundException("Task not found with id:" + 99L));

        String apiPath= API_BASE_PATH + "/upload/" + 99L;
        mockMvc.perform(multipart(apiPath)
                        .file(file))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id:" + 99L));
    }

    @Test
    void downloadFile_ShouldReturnOk() throws Exception {

        byte[] fileBytes = "dummy file content".getBytes();

        AttachmentResponse attachmentResponse= new AttachmentResponse();
        attachmentResponse.setId(1L);

        when(attachmentService.downloadAttachment(eq(attachmentResponse.getId()))).thenReturn(fileBytes);

        String apiPath= API_BASE_PATH + "/download/" + attachmentResponse.getId() ;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileBytes))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    void downloadFile_ShouldReturn404_WhenAttachmentNotFound() throws Exception {
        when(attachmentService.downloadAttachment(100L))
                .thenThrow(new NotFoundException("attachment not found with id:" + 100L));

        String apiPath= API_BASE_PATH + "/download/" + 100L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("attachment not found with id:" + 100L));
    }

    @Test
    void getAttachmentsByTaskId_ShouldReturnOk() throws Exception {
        TaskResponse taskResponse= new TaskResponse();
        taskResponse.setId(1L);

        AttachmentResponse attachmentResponse1 = new AttachmentResponse();
        attachmentResponse1.setId(1L);
        attachmentResponse1.setFileName("file1.txt");
        attachmentResponse1.setTask(taskResponse);

        AttachmentResponse attachmentResponse2 = new AttachmentResponse();
        attachmentResponse2.setId(2L);
        attachmentResponse2.setFileName("file2.txt");
        attachmentResponse2.setTask(taskResponse);

        when(attachmentService.getAttachmentsByTaskId(eq(taskResponse.getId())))
                .thenReturn(List.of(attachmentResponse1, attachmentResponse2));

        String apiPath= API_BASE_PATH + "/task/" + taskResponse.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].fileName").value("file1.txt"))
                .andExpect(jsonPath("$[1].fileName").value("file2.txt"));
    }

    @Test
    void getAttachmentsByTaskId_ShouldReturn404_WhenTaskNotFound() throws Exception {
        when(attachmentService.getAttachmentsByTaskId(999L))
                .thenThrow(new NotFoundException("Task not found with id:" + 999L));

        String apiPath= API_BASE_PATH + "/task/" + 999L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id:" + 999L));
    }

}
