package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Attachment;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.AttachmentMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.AttachmentRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.TaskRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.AttachmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final AttachmentMapper attachmentMapper;

    private final String uploadPath = "src/main/resources/static/attachments";

    @CacheEvict(value = "attachments", allEntries = true)
    @Override
    public AttachmentResponse uploadFileToTask(Long taskId, MultipartFile file) throws NotFoundException {
        Task task=taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task not found with id:" + taskId));
        String fileName= UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path path= Paths.get(uploadPath,fileName);
        try {
            Files.write(path,file.getBytes());
        }catch (IOException e){
            throw new RuntimeException("File upload failed",e);
        }
        Attachment attachment=new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(path.toString());
        attachment.setTask(task);
        attachmentRepository.save(attachment);
        return attachmentMapper.asOutput(attachment);
    }

    @Override
    public byte[] downloadAttachment(Long attachmentId) throws NotFoundException {
        Optional<Attachment> attachmentIsDb=attachmentRepository.findById(attachmentId);
        if (attachmentIsDb.isEmpty()){
            throw new NotFoundException("attachment not found with id:" + attachmentId);
        }try {
            return Files.readAllBytes(Paths.get(attachmentIsDb.get().getFilePath()));
        }catch (IOException e){
            throw new RuntimeException("File read error",e);
        }
    }

    @Cacheable(value = "attachments", key = "#taskId")
    @Override
    public List<AttachmentResponse> getAttachmentsByTaskId(Long taskId) throws NotFoundException {
        Task task=this.taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Task not found with id:" + taskId));
        return attachmentRepository.findByTaskId(task.getId())
                .stream()
                .map(attachmentMapper::asOutput)
                .collect(Collectors.toList());
    }
}
