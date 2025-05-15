package fun.example.parcel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fun.example.parcel.db.dto.UserDto;
import fun.example.parcel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<UserService.SignupResponse> postMethodName(@RequestBody @Valid UserService.SignupRequest entity) {
        return userService.signup(entity);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponse<List<UserDto>>> getUsers() {
        try {
            List<UserDto> userList = userService.getAllUsers();
            return ResponseEntity.ok().body(new CustomResponse<>(false, "User fetched", userList));
        } catch (Exception e) {
            logger.error("Error fetching the users {} ", e);
            return ResponseEntity.badRequest().body(new CustomResponse<>(false, "Some Server error", null));
        }
    }

}
