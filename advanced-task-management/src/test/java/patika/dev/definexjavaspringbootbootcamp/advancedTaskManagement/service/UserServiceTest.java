package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.AuthenticationServiceImpl;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.JwtServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationService)
                .build();
    }

    @Test
    void save_ShouldReturnToken_WhenUserNotExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@example.com");
        request.setPassword("123");
        request.setRole(UserRole.TEAM_MEMBER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(jwtServiceImpl.generateToken(any(User.class))).thenReturn("mock-token");

        UserResponse result = authenticationService.save(request);

        assertEquals("mock-token", result.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void save_ShouldThrowException_WhenUserAlreadyExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(MethodArgumentNotValidException.class, () -> authenticationService.save(request));
    }

    @Test
    void auth_ShouldReturnToken_WhenCredentialsValid() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("123");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceImpl.generateToken(user)).thenReturn("mock-example-bearer-token");

        UserResponse response = authenticationService.auth(request);

        assertEquals("mock-example-bearer-token", response.getToken());
    }

    @Test
    void auth_ShouldThrowException_WhenAuthFails() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong");

        doThrow(BadCredentialsException.class).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(Exception.class, () -> authenticationService.auth(request));
    }

}
