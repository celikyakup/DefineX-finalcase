package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByTitleAndDescriptionAndDepartment(String title, String description, String department);
}
