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
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    private static final String API_BASE_PATH = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findUsers_ShouldReturnOk() throws Exception {
        UserInfoResponse user1 = new UserInfoResponse(1L,"test","test@gmail.com", UserRole.TEAM_MEMBER);
        UserInfoResponse user2= new UserInfoResponse(2L,"test2","test2@gmail.com", UserRole.TEAM_MEMBER);
        when(userService.getAllUsers()).thenReturn(List.of(user1,user2));

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("test2@gmail.com"));
    }

    @Test
    void createUser_ShouldReturnOk() throws Exception {
        UserInfoRequest request= new UserInfoRequest();
        request.setName("test");
        request.setEmail("test@gmail.com");
        request.setPassword("password");
        request.setRole(UserRole.PROJECT_MANAGER);

        UserInfoResponse response= new UserInfoResponse(1L,"test","test@gmail.com",UserRole.PROJECT_MANAGER);
        when(userService.saveUser(any(UserInfoRequest.class))).thenReturn(response);

        String apiPath = API_BASE_PATH ;
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));

    }

   @Test
    void findUserById_ShouldReturnOk() throws Exception{
        UserInfoResponse response=new UserInfoResponse(1L,"test","test@gmail.com",UserRole.TEAM_MEMBER);
        when(userService.getUserById(1L)).thenReturn(response);

        String apiPath= API_BASE_PATH + "/" + response.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception{
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setEmail("test@test.com");
        userInfoRequest.setRole(UserRole.TEAM_MEMBER);
        userInfoRequest.setPassword("test");
        userInfoRequest.setName("test");

        UserInfoResponse userInfoResponse=new UserInfoResponse(1L,"test2","test2@test.com",UserRole.TEAM_MEMBER);

        when(userService.updateUser(eq(1L),any(UserInfoRequest.class))).thenReturn(userInfoResponse);

        String apiPath= API_BASE_PATH + "/" + userInfoResponse.getId();
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test2"))
                .andExpect(jsonPath("$.email").value("test2@test.com"));
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception{
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setEmail("test@test.com");
        userInfoRequest.setRole(UserRole.TEAM_MEMBER);
        userInfoRequest.setPassword("test");
        userInfoRequest.setName("test");

        UserInfoResponse userInfoResponse=new UserInfoResponse(1L,"test","test@test.com",UserRole.TEAM_MEMBER);

        doNothing().when(userService).delete(userInfoResponse.getId());

        String apiPath= API_BASE_PATH + "/" + userInfoResponse.getId();
        mockMvc.perform(delete(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setName("test");
        userInfoRequest.setEmail("test");
        userInfoRequest.setPassword("abc");

        String apiPath= API_BASE_PATH + "/1";
        mockMvc.perform(put(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setName("test");
        userInfoRequest.setPassword("abc");
        userInfoRequest.setEmail("test");
        userInfoRequest.setRole(UserRole.TEAM_MEMBER);

        mockMvc.perform(post(API_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ShouldReturn404WhenUserNotFound() throws Exception {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setName("test");
        userInfoRequest.setPassword("abc");
        userInfoRequest.setEmail("test@gmail.com");
        userInfoRequest.setRole(UserRole.TEAM_MEMBER);

        when(userService.updateUser(eq(200L), any()))
                .thenThrow(new NotFoundException("User not found!"));

        String apiPath = API_BASE_PATH + "/200";
        mockMvc.perform(put(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

    @Test
    void findUserById_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        when(userService.getUserById(990L)).thenThrow(new NotFoundException("User not found!"));

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

    @Test
    void deleteUserById_ShouldReturnValidationErrorWhenInputIsInvalid() throws Exception{
        doThrow(new NotFoundException("User not found!")).when(userService).delete(990L);

        String apiPath= API_BASE_PATH + "/" + 990L;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found!"));
    }

}
