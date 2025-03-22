package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class AdvancedTaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedTaskManagementApplication.class, args);
	}

}
