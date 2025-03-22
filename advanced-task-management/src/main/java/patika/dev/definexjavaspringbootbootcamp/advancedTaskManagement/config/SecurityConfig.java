package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.security.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry->{
                    registry.requestMatchers(HttpMethod.POST,"/v1/auth/**").permitAll();
                    registry.requestMatchers("/v1/users/**").hasAnyAuthority("PROJECT_MANAGER");
                    registry.requestMatchers( "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**").permitAll();
                    registry.requestMatchers(HttpMethod.GET,"/v1/users/{id}").permitAll();
                    registry.requestMatchers(HttpMethod.PUT,"/v1/users/{id}").permitAll();
                    registry.requestMatchers(HttpMethod.POST,"/v1/tasks/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.PUT,"/v1/tasks/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.PATCH,"/v1/tasks/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.DELETE,"/v1/tasks/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.GET,"v1/tasks/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/tasks/{userId}").hasAuthority("TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/project/**").hasAuthority("PROJECT_MANAGER");
                    registry.requestMatchers(HttpMethod.POST,"/v1/project/**").hasAuthority("PROJECT_MANAGER");
                    registry.requestMatchers(HttpMethod.DELETE,"/v1/project/**").hasAuthority("PROJECT_MANAGER");
                    registry.requestMatchers(HttpMethod.PUT,"/v1/project/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEAD");
                    registry.requestMatchers(HttpMethod.GET,"/v1/project/{id}").hasAuthority("TEAM_LEADER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/comments/task/{taskId}").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.POST,"/v1/comments/task/{taskId}").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.DELETE,"/v1/comments/{commentId}").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEAD");
                    registry.requestMatchers(HttpMethod.POST,"/v1/attachments/upload/{taskId}").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/attachments/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/project-state-change-history/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.requestMatchers(HttpMethod.GET,"/v1/task-state-history/**").hasAnyAuthority("PROJECT_MANAGER","TEAM_LEADER","TEAM_MEMBER");
                    registry.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout
                        .logoutUrl("/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())))
                .build();
    }


}
