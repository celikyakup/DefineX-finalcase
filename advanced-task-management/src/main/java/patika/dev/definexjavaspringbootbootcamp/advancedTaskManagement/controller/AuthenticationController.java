package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.AuthenticationServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@RequestBody UserInfoRequest userInfoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.save(userInfoRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> auth(@RequestBody UserRequest userRequest) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.auth(userRequest));
    }
}
