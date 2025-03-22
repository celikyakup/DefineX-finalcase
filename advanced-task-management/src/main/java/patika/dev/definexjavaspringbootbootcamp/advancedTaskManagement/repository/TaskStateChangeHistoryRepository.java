package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.TaskStateChangeHistory;

import java.util.List;

public interface TaskStateChangeHistoryRepository extends JpaRepository<TaskStateChangeHistory,Long> {
    @Query("SELECT t FROM TaskStateChangeHistory t WHERE t.task.id = :id")
    List<TaskStateChangeHistory> findByTaskId(@Param("id") Long id);
}
