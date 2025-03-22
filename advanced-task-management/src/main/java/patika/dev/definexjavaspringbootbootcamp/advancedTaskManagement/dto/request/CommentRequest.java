package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.TaskResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequest implements Serializable {
    @NotBlank(message = "Comment cannot be empty!")
    private String content;

    private LocalDateTime createdAt;

    private boolean deleted;

    private TaskResponse task;

    private UserInfoResponse user;
}
