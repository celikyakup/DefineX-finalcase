package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    User asEntity(UserInfoRequest userInfoRequest);

    UserInfoResponse asOutput(User user);

    List<UserInfoResponse> asOutput(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email",ignore = true)
    void update(@MappingTarget User entity, UserInfoRequest userInfoRequest);
}
