package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityForHistory {

    @CreatedDate
    @Column(updatable = false, name = "updated_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false,name = "updated_by")
    private String createdBy;

    @Column(name = "deleted")
    private boolean deleted = false;

    @PrePersist
    public void prePersist(){
        this.createdBy= SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
