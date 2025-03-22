package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base.BaseEntity;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.ProjectStatus;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "department")
    private String department;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "projects_users",joinColumns = @JoinColumn(name = "project_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> teamMembers;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Task> taskList;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ProjectStateChangeHistory> projectStateChangeHistorieList;
}
