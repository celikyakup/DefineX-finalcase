package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.config.GlobalExceptionHandler;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.AuthenticationServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {
    private static final String API_BASE_PATH = "/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationServiceImpl authenticationServiceImpl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_ShouldReturnToken_WhenUserSavedSuccessfully() throws Exception {
        UserInfoRequest request = new UserInfoRequest();
        request.setName("test");
        request.setEmail("test@example.com");
        request.setPassword("test");
        request.setRole(UserRole.TEAM_MEMBER);

        UserResponse response = new UserResponse("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YWt1cEBnbWFpbC5jb20iLCJpYXQiOjE3NDIyOTQ2NDUsImV4cCI6MjUwODkwNDI4ODg1NzYwMH0.FLjcXPsHoYeh2ZM8dc69nwEVfmTm9Gd03x5g6x5wGws");

        when(authenticationServiceImpl.save(any(UserInfoRequest.class))).thenReturn(response);

        String apiPath= API_BASE_PATH + "/register";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YWt1cEBnbWFpbC5jb20iLCJpYXQiOjE3NDIyOTQ2NDUsImV4cCI6MjUwODkwNDI4ODg1NzYwMH0.FLjcXPsHoYeh2ZM8dc69nwEVfmTm9Gd03x5g6x5wGws"));
    }

    @Test
    void register_ShouldReturn400_WhenUserAlreadyExists() throws Exception {
        UserInfoRequest request = new UserInfoRequest();
        request.setName("test");
        request.setEmail("test@example.com");
        request.setPassword("test");
        request.setRole(UserRole.TEAM_MEMBER);

        when(authenticationServiceImpl.save(any(UserInfoRequest.class)))
                .thenThrow(new MethodArgumentNotValidException("This username has already been registered!"));

        String apiPath= API_BASE_PATH + "/register";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("This username has already been registered!"));
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("test");

        UserResponse response = new UserResponse();
        response.setToken("mock-jwt-token");

        when(authenticationServiceImpl.auth(any(UserRequest.class))).thenReturn(response);

        String apiPath= API_BASE_PATH + "/login";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(status().isCreated());
    }

}
