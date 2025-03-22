package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.Mapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Attachment;

@Mapper
public interface AttachmentMapper {

    AttachmentResponse asOutput(Attachment attachment);

}
