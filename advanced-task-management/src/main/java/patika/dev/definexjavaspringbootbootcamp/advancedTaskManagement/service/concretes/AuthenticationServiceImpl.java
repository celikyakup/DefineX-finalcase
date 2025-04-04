package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.core.enums.UserRole;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.User;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.MethodArgumentNotValidException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.exception.UserNameNotFoundException;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.repository.UserRepository;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.AuthenticationService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public UserResponse save(UserInfoRequest userInfoRequest) throws MethodArgumentNotValidException{
        User user=new User();
        user.setName(userInfoRequest.getName());
        user.setEmail(userInfoRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userInfoRequest.getPassword()));
        user.setRole(UserRole.TEAM_MEMBER);

        Optional<User> isDbExist=this.userRepository.findByEmail(userInfoRequest.getEmail());
        if (isDbExist.isEmpty()){
            this.userRepository.save(user);
            var token= jwtServiceImpl.generateToken(user);
            return new UserResponse(token);
        }
        throw new MethodArgumentNotValidException("This username has already been registered!");
    }

    @Override
    public UserResponse auth(UserRequest userRequest) throws Exception{
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(),userRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new Exception("Mail or password is incorrect!");
        }
        User user= userRepository.findByEmail(userRequest.getEmail()).orElseThrow(()->new UserNameNotFoundException("Mail or password is incorrect!"));
        String token= jwtServiceImpl.generateToken(user);
        return new UserResponse(token);
    }
}
