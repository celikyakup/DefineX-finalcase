package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import org.springframework.web.multipart.MultipartFile;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface AttachmentService {
    AttachmentResponse uploadFileToTask(Long taskId, MultipartFile file) throws NotFoundException;
    byte[] downloadAttachment(Long attachmentId) throws NotFoundException;
    List<AttachmentResponse> getAttachmentsByTaskId(Long taskId) throws NotFoundException;
}
