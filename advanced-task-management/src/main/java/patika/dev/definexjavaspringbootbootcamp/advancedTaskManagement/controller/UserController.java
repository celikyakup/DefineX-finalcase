package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.UserInfoRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.UserInfoResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserInfoResponse> createUser(@Valid @RequestBody UserInfoRequest userInfoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.saveUser(userInfoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserInfoRequest userInfoRequest){
        return ResponseEntity.ok(this.userService.updateUser(id, userInfoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
