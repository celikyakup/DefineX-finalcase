package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.JwtServiceImpl;

import java.util.Base64;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jwtService)
                .build();
    }

    @Test
    void generateToken_ShouldReturnToken() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", Base64.getEncoder().encodeToString("secretsecretsecretsecretsecretsecret".getBytes()));
        when(userDetails.getUsername()).thenReturn("test@example.com");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void tokenControl_ShouldReturnTrue_WhenValidToken() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", Base64.getEncoder().encodeToString("secretsecretsecretsecretsecretsecret".getBytes()));
        when(userDetails.getUsername()).thenReturn("test@example.com");

        String token = jwtService.generateToken(userDetails);
        boolean result = jwtService.tokenControl(token, userDetails);
        assertTrue(result);
    }

    @Test
    void blackList_ShouldAddAndCheck() {
        String token = "example.bearer.token.value";
        jwtService.addToBlackList(token);
        assertTrue(jwtService.isBlackListed(token));
    }
}
