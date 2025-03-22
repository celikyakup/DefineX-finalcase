package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Attachment;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.AttachmentMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.TaskMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.AttachmentRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.AttachmentServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class AttachmentServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @Mock
    private TaskMapper taskMapper;

    private final String uploadPath = "src/main/resources/static/attachments";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentService)
                .build();
    }

    @Test
    void uploadFileToTask_ShouldThrowException_WhenTaskNotFound() {
        MultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "data".getBytes());
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attachmentService.uploadFileToTask(1L, file));
    }

    @Test
    void downloadAttachment_ShouldReturnBytes_WhenFileExists() throws IOException {
        Attachment att = new Attachment();
        att.setFilePath(uploadPath + "/test.txt");
        Files.write(Paths.get(att.getFilePath()), "abc".getBytes());

        when(attachmentRepository.findById(1L)).thenReturn(Optional.of(att));
        byte[] result = attachmentService.downloadAttachment(1L);
        assertNotNull(result);
    }

    @Test
    void downloadAttachment_ShouldThrow404_WhenNotExist() {
        when(attachmentRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> attachmentService.downloadAttachment(999L));
    }

    @Test
    void getAttachmentsByTaskId_ShouldReturnList() {
        Task task = new Task();
        task.setId(1L);
        Attachment attachment = new Attachment();
        AttachmentResponse dto = new AttachmentResponse();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(attachmentRepository.findByTaskId(task.getId())).thenReturn(List.of(attachment));
        when(attachmentMapper.asOutput(attachment)).thenReturn(dto);

        List<AttachmentResponse> result = attachmentService.getAttachmentsByTaskId(task.getId());
        assertEquals(1, result.size());
    }

}
