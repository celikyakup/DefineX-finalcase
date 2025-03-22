package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.security.JwtBlackListInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtBlackListInterceptor jwtBlackListInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtBlackListInterceptor);
    }
}
