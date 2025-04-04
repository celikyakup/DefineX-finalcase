package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.NotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper.UserMapper;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.messaging.RabbitMQSender;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RabbitMQSender rabbitMQSender;

    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "users",key = "all")
    })
    @Override
    public UserInfoResponse saveUser(UserInfoRequest userInfoRequest) throws MethodArgumentNotValidException {
        Optional<User> isUserExist=this.userRepository. findByEmail(userInfoRequest.getEmail());
        if (isUserExist.isPresent()){
            throw new MethodArgumentNotValidException("User already create in system.");
        }
        userInfoRequest.setPassword(passwordEncoder.encode(userInfoRequest.getPassword()));
        User user=userRepository.save(userMapper.asEntity(userInfoRequest));
        rabbitMQSender.sendCacheInvalidate("users::all");
        return this.userMapper.asOutput(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users", allEntries = true)
    })
    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest userInfoRequest) throws NotFoundException {
        Optional<User> userFromDb=this.userRepository.findById(id);
        if (userFromDb.isEmpty()){
            throw new NotFoundException("User not found with id:" + id);
        }
        User user=userFromDb.get();
        this.userMapper.update(user, userInfoRequest);
        return this.userMapper.asOutput(this.userRepository.save(user));
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserInfoResponse getUserById(Long id) throws NotFoundException {
        return this.userMapper.asOutput(this.userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found with id:" + id)));
    }

    @Override
    @Cacheable(value = "users")
    public List<UserInfoResponse> getAllUsers() {
        return this.userMapper.asOutput(this.userRepository.findAll());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users", allEntries = true)
    })
    public void delete(Long id) throws NotFoundException {
        Optional<User> userFromDb=this.userRepository.findById(id);
        if (userFromDb.isEmpty()){
            throw new NotFoundException("User not found with id:" + id);
        } if (userFromDb.get().isDeleted()){
            throw new MethodArgumentNotValidException("The user has already been deleted!");
        }
        userFromDb.get().setDeleted(true);
        this.userRepository.save(userFromDb.get());
    }
}
