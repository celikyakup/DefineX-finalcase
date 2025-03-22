package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse implements Serializable {
    private Long id;

    private String filePath;

    private String fileName;

    private TaskResponse task;

    private String uploadedAt;
}
