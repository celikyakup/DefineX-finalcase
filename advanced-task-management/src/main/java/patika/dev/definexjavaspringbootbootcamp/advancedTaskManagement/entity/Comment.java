package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base.BaseEntity;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comment")
public class Comment extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name ="user_id", referencedColumnName = "user_id")
    private User user;
}
