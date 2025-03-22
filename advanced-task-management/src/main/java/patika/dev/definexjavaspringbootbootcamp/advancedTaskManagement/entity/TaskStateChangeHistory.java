package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base.BaseEntityForHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_state_change_history")
public class TaskStateChangeHistory extends BaseEntityForHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "old_state")
    private TaskStatus oldState;

    @Column(name = "new_state")
    private TaskStatus newState;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

}
