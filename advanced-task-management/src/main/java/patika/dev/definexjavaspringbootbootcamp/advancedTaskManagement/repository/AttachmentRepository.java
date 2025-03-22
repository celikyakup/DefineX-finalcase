package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    List<Attachment> findByTaskId(Long taskId);
}
