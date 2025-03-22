package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base.BaseEntityForHistory;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_state_change_history")
public class ProjectStateChangeHistory extends BaseEntityForHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "old_state")
    private ProjectStatus oldState;

    @Column(name = "new_state")
    private ProjectStatus newState;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

}
