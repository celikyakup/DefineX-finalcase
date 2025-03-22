package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @CreatedBy
    @Column(updatable = false,name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted")
    private boolean deleted=false;

    @PrePersist
    public void prePersist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            this.createdBy = authentication.getName();
        } else {
            this.createdBy = "SYSTEM";
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedBy=SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
