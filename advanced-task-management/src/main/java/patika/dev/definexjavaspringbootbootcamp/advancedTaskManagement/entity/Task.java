package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base.BaseEntity;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskPriority;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.TaskStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

    @Column(name = "reason")
    private String reason;

    @Column(name = "user_story")
    private String userStory;

    @Column(name = "accept_criteria")
    private String acceptCriteria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id",referencedColumnName = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> commentList;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Attachment> attachmentList;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<TaskStateChangeHistory> stateChangeHistories;
}
