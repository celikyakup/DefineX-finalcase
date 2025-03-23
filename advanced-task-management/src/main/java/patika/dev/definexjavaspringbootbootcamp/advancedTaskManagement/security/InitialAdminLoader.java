package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class InitialAdminLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()){
            User admin= new User();
            admin.setName("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(UserRole.PROJECT_MANAGER);
            userRepository.save(admin);
        }
    }
}
