package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.JwtServiceImpl;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.LogoutService;

import static org.mockito.Mockito.*;

public class LogoutServiceTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logoutService)
                .build();
    }

    @Test
    void logout_ShouldAddTokenToBlacklist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");

        logoutService.logout(request, response, null);
        verify(jwtService).addToBlackList("test-token");
    }

    @Test
    void logout_ShouldIgnoreIfNoTokenProvided() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, null);
        verify(jwtService, never()).addToBlackList(anyString());
    }
}
