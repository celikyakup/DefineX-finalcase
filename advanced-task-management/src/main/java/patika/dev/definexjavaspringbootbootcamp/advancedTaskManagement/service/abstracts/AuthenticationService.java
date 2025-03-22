package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;


public interface AuthenticationService {

    UserResponse save(UserInfoRequest userInfoRequest) throws MethodArgumentNotValidException;
    UserResponse auth(UserRequest userRequest) throws Exception;
}
