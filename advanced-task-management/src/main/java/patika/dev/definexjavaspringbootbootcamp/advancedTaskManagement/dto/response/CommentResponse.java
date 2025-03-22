package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse implements Serializable {
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private boolean deleted;

    private TaskResponse task;

    private UserInfoResponse user;
}
