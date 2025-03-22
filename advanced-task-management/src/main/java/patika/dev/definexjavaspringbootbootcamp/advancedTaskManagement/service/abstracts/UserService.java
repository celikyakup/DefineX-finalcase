package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts;

import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;

import java.util.List;

public interface UserService {
    UserInfoResponse saveUser(UserInfoRequest userInfoRequest) throws MethodArgumentNotValidException;
    UserInfoResponse updateUser(Long id, UserInfoRequest userInfoRequest) throws NotFoundException;
    UserInfoResponse getUserById(Long id) throws NotFoundException;
    List<UserInfoResponse> getAllUsers();
    void delete(Long id) throws NotFoundException;

}
