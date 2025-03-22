package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.ProjectStateChangeHistory;

import java.util.List;

public interface ProjectStateChangeHistoryRepository extends JpaRepository<ProjectStateChangeHistory,Long> {
    @Query("SELECT p FROM ProjectStateChangeHistory p WHERE p.project.id = :id")
    List<ProjectStateChangeHistory> findByProjectId(@Param("id") Long id);
}
