package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByTitleAndDescriptionAndReasonAndUserStoryAndAcceptCriteria(String title, String description, String reason, String userStory, String acceptCriteria);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    List<Task> findByProjectId(@Param("projectId") Long projectId);
}
